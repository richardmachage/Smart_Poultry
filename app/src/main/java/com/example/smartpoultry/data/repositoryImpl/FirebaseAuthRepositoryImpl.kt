package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_EMAIL_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_NAME_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_PHONE_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_ROLE_KEY
import com.example.smartpoultry.data.dataSource.remote.firebase.FARMS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.USERS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.models.Farm
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val dataStore: AppDataStore
) : FirebaseAuthRepository {

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
                    val user = User(email = email, role = role, farmId = newFarm.id)
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
                    val user = User(email= email, role = role, farmId = farmId, phone = "", name = "")
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
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    // Optionally, you can perform additional checks or fetch user details from Firestore if needed.
                    // For simplicity, we're just checking if the Firebase user is not null.
                    firebaseFirestore
                        .collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            val user = documentSnapshot.toObject(User::class.java)

                            //save user details  to datastore
                            //user Farm
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user ->
                                    dataStore.saveData(FARM_ID_KEY, user.farmId)
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
            firebaseAuth.sendPasswordResetEmail(email).await()
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


    override fun logOut() {
        firebaseAuth.signOut()
    }
}