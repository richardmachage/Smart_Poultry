package com.example.smartpoultry.data.repositoryImpl

import android.util.Log
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.datastore.FARM_NAME_KEY
import com.example.smartpoultry.data.dataSource.datastore.IS_PASSWORD_RESET_KEY
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.datastore.USER_EMAIL_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_NAME_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_PHONE_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_ROLE_KEY
import com.example.smartpoultry.data.dataSource.remote.firebase.FARMS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.USERS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.models.Farm
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.functions.functions
import com.google.protobuf.Internal.BooleanList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

var isPasswordReset: Boolean? = null

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
) : FirebaseAuthRepository {

    val functions = Firebase.functions
    override suspend fun signUp(
        email: String,
        password: String,
        role: String,
        farmName: String
    ): Result<Boolean> = coroutineScope {
        //step 1. Create user -> using email and password
        //step 2. create farm and retrieve farm ID
        //step 3. create the user in the users collection in firestore
        val deffered = async(Dispatchers.IO) {
            try {
                //step 1 create user
                val authResult =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user
                //step 2 create farm
                val newFarm = firebaseFirestore.collection(FARMS_COLLECTION).document()
                newFarm.set(Farm(name = farmName, id = newFarm.id, superUserEmail = email)).await()
                //step 3 add created user to the Main Users Collection
                firebaseUser?.let {
                    val user = User(
                        userId = firebaseUser.uid,
                        email = email,
                        role = role,
                        farmId = newFarm.id,
                        passwordReset = true
                    )
                    firebaseFirestore.collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .set(user)
                        .await()
                    Result.success(true)
                } ?: Result.failure(Exception("Firebase user is null"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        deffered.await()
    }


    override suspend fun registerUser(
        email: String,
        password: String,
        role: String,
        farmId: String
    ): Result<Boolean> = coroutineScope {

        val deferred = async(Dispatchers.IO) {
            try {
                val authResult =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                firebaseUser?.let {
                    val user =
                        User(
                            userId = firebaseUser.uid,
                            email = email,
                            role = role,
                            farmId = farmId,
                            phone = "",
                            name = "",
                            passwordReset = false
                        )
                    firebaseFirestore.collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .set(user)
                        .await()
                    Result.success(true)
                } ?: Result.failure(Exception("Firebase user is null"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        deferred.await()
    }


    override suspend fun logIn(email: String, password: String): Result<Boolean> = coroutineScope {
        val deferred = async(Dispatchers.IO) {
            try {
                //try logging in
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user


                if (firebaseUser != null) {
                    // Perform additional checks or fetch user details from Firestore if needed.
                    firebaseFirestore
                        .collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            val user = documentSnapshot.toObject(User::class.java)
                            //check status of password reset
                            user?.let {
                                //then also save to local
                                preferencesRepo.saveData(
                                    IS_PASSWORD_RESET_KEY,
                                    it.passwordReset.toString()
                                )
                            }

                            //save user other details  to datastore
                            //user Farm
                            preferencesRepo.saveData(FARM_ID_KEY, user?.farmId.toString())
                            Log.d("Farm", "saving farm to datastore: ${user?.farmId}")

                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user ->
                                    dataStore.saveData(FARM_ID_KEY, user.farmId)
                                    Log.d("Farm", "saving farm to datastore: ${user.farmId}")
                                }
                            }

                            //user Role
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user ->
                                    dataStore.saveData(USER_ROLE_KEY, user.role)
                                }
                            }

                            //User Name
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user ->
                                    dataStore.saveData(USER_NAME_KEY, user.name)
                                }
                            }
                            //User email
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user ->
                                    dataStore.saveData(USER_EMAIL_KEY, user.email)
                                }
                            }
                            //User phone
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user ->
                                    dataStore.saveData(USER_PHONE_KEY, user.phone)
                                }
                            }

                        }

                    Result.success(true)
                } else {
                    Result.failure(Exception("Firebase user is null"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
        deferred.await()
    }

    override suspend fun resetPassword(email: String): Result<Boolean> = coroutineScope {
        try {
            // Await the completion of the password reset email sending
            firebaseAuth.sendPasswordResetEmail(email).addOnFailureListener {
                Throwable(message = it.message, cause = it)
            }.await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun editUserName(name: String): Result<Boolean> {
        val result = CompletableDeferred<Boolean>()
        firebaseFirestore.collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString())//firebaseAuth.uid.toString())
            .update("name", name)
            .addOnSuccessListener {
                result.complete(true)
            }
            .addOnFailureListener {
                result.completeExceptionally(it)
            }

        return if (result.await()) Result.success(true) else Result.failure(result.getCompletionExceptionOrNull()!!)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun updateIsPasswordChanged(): Result<Boolean> {
        val result = CompletableDeferred<Boolean>()
        firebaseFirestore.collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString())//firebaseAuth.uid.toString())
            .update("passwordReset", true)
            .addOnSuccessListener {
                result.complete(true)
            }
            .addOnFailureListener {
                result.completeExceptionally(it)
            }

        return if (result.await()) Result.success(true) else Result.failure(result.getCompletionExceptionOrNull()!!)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun editEmail(email: String): Result<Boolean> {
        val completableDeferred = CompletableDeferred<Boolean>()
        firebaseAuth.currentUser?.verifyBeforeUpdateEmail(email)
            ?.addOnSuccessListener {
                firebaseFirestore.collection(USERS_COLLECTION)
                    .document(firebaseAuth.currentUser?.uid.toString())
                    .update("email", email)
                    .addOnSuccessListener {
                        completableDeferred.complete(true)
                    }
                    .addOnFailureListener {
                        completableDeferred.completeExceptionally(it)
                    }
            }
            ?.addOnFailureListener {
                completableDeferred.completeExceptionally(it)
            }
        return if (completableDeferred.await()) Result.success(true) else Result.failure(
            completableDeferred.getCompletionExceptionOrNull()!!
        )
    }

    override suspend fun editUserRole(email: String, role: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun editPhone(phone: String): Result<Boolean> {
        val completableDeferred = CompletableDeferred<Boolean>()
        firebaseFirestore.collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString())
            .update("phone", phone)
            .addOnSuccessListener {
                completableDeferred.complete(true)
            }
            .addOnFailureListener {
                completableDeferred.completeExceptionally(it)
            }

        return if (completableDeferred.await()) Result.success(true) else Result.failure(
            completableDeferred.getCompletionExceptionOrNull()!!
        )
    }

    override suspend fun getFarm(): String {
        //1. check if name exists locally
        //2. if not, read from remote and store locally
        //3. return value from local

        var farmName = preferencesRepo.loadData(FARM_NAME_KEY) ?: ""
        var farmId = preferencesRepo.loadData(FARM_ID_KEY) ?: ""

        if (farmName.isNotBlank()) {
            //farm name exists locally, we return here
            return farmName
        } else {
            //farm name does not exist locally so we red from remote source
            val docsnapshot = firebaseFirestore.collection(FARMS_COLLECTION).document(farmId)
                .get()
                .addOnSuccessListener {
                    val farm = it.toObject(Farm::class.java)
                    farm?.let {
                        preferencesRepo.saveData(FARM_NAME_KEY, it.name)
                    }
                }
                .await()
            return preferencesRepo.loadData(FARM_NAME_KEY) ?: ""
        }

    }

    override suspend fun getFarmEmployees(farmId: String): Result<List<User>> {
        val listOfEmployees = mutableListOf<User>()

        try {
            val querySnapshot = firebaseFirestore.collection(USERS_COLLECTION)
                .whereEqualTo("farmId", farmId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val user = document.toObject<User>()
                user?.let {
                    listOfEmployees.add(it)
                }
            }
            return Result.success(listOfEmployees)
        } catch (e: Exception) {
            Log.d("get employees", e.message.toString())
            return Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<String> {
        var completableDeferred = CompletableDeferred<Result<String>>()

        // Ensure the current user is authenticated
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            //now delete employee
            // Create the data to pass to the Cloud Function
            val data = hashMapOf(
                "userId" to userId
            )

            //call the cloud function
            functions.getHttpsCallable("deleteUser")
                .call(data)
                .addOnSuccessListener {
                    completableDeferred.complete(Result.success("User deleted successfully"))
                }
                .addOnFailureListener{
                    completableDeferred.complete(Result.failure(it))
                }
                .await()
        }?: run {
            completableDeferred.complete(Result.failure(Throwable(message = "Deletion failed, Log into app afresh and try again")))
        }


        return completableDeferred.await()
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }
}