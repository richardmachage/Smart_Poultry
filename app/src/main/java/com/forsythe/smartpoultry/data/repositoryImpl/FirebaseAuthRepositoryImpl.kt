package com.forsythe.smartpoultry.data.repositoryImpl

import android.util.Log
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.Farm
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User
import com.forsythe.smartpoultry.domain.repository.FirebaseAuthRepository
import com.forsythe.smartpoultry.utils.ACCESS_LEVEL
import com.forsythe.smartpoultry.utils.CONSUCUTIVE_DAYS_KEY
import com.forsythe.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.forsythe.smartpoultry.utils.EGG_COLLECTION_ACCESS
import com.forsythe.smartpoultry.utils.FARMS_COLLECTION
import com.forsythe.smartpoultry.utils.FARM_COUNTRY_KEY
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.forsythe.smartpoultry.utils.FARM_NAME_KEY
import com.forsythe.smartpoultry.utils.FARM_SUPER_USER_EMAIL
import com.forsythe.smartpoultry.utils.IS_PASSWORD_RESET_KEY
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_USERS_ACCESS
import com.forsythe.smartpoultry.utils.PAST_DAYS_KEY
import com.forsythe.smartpoultry.utils.REPEAT_INTERVAL_KEY
import com.forsythe.smartpoultry.utils.THRESHOLD_RATIO_KEY
import com.forsythe.smartpoultry.utils.USERS_COLLECTION
import com.forsythe.smartpoultry.utils.USER_EMAIL_KEY
import com.forsythe.smartpoultry.utils.USER_FIRST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_LAST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_PHONE_KEY
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.functions.functions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

var isPasswordReset: Boolean? = null

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    // private val dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
) : FirebaseAuthRepository {


    val functions = Firebase.functions
    override suspend fun signUp(
        email: String,
        password: String,
        role: String,
        farmName: String,
        firstName: String,
        lastName : String,
        phone: String,
        country : String,
        gender : String
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
                newFarm.set(Farm(name = farmName, id = newFarm.id, superUserEmail = email, country = country)).await()
                //step 3 add created user to the Users Collection
                firebaseUser?.let {
                    val user = User(
                        userId = firebaseUser.uid,
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        email = email,
                        farmId = newFarm.id,
                        passwordReset = true,
                        gender = gender
                    )
                    firebaseFirestore.collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .set(user)
                        .addOnSuccessListener {
                            //step 4: add the AccessLevel subcollection
                            CoroutineScope(Dispatchers.IO).launch {
                                firebaseFirestore.collection(USERS_COLLECTION)
                                    .document(firebaseUser.uid).collection(ACCESS_LEVEL)
                                    .document(firebaseUser.uid + "accessLevel")
                                    .set(
                                        AccessLevel(
                                            collectEggs = true,
                                            editHenCount = true,
                                            manageBlocksCells = true,
                                            manageUsers = true,

                                            )
                                    )
                                    .await()
                            }
                        }
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
        firstName: String,
        lastName: String,
        gender: String,
        email: String,
        password: String,
        farmId: String,
        phone: String,
        accessLevel: AccessLevel
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
                            firstName = firstName,
                            lastName = lastName,
                            gender = gender,
                            email = email,
                            farmId = farmId,
                            phone = phone,
                            passwordReset = false
                        )
                    firebaseFirestore.collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .set(user)
                        .addOnSuccessListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                firebaseFirestore.collection(USERS_COLLECTION)
                                    .document(firebaseUser.uid).collection(ACCESS_LEVEL)
                                    .document(firebaseUser.uid + "accessLevel")
                                    .set(accessLevel)
                                    .await()
                            }
                        }
                        .await()
                    Result.success(true)
                } ?: Result.failure(Exception("Firebase user is null"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        deferred.await()
    }

    suspend fun fetchUserDetails(firebaseUser: FirebaseUser) : User?{
            val task = firebaseFirestore.collection(USERS_COLLECTION).document(firebaseUser.uid).get()
            val documentSnapshot = task.await()

            if (task.isSuccessful){
                return documentSnapshot.toObject(User::class.java)
            }else{
                Throwable(task.exception)
                return null
            }

    }

    suspend fun fetchUserAccessLevel(userId: String):AccessLevel?{
        val task = firebaseFirestore.collection(USERS_COLLECTION).document(userId).collection(ACCESS_LEVEL).document(userId + "accessLevel").get()
        val documentSnapShot = task.await()

        if (task.isSuccessful){
            return  documentSnapShot.toObject(AccessLevel::class.java)
        }else{
            Throwable(task.exception)
            return null
        }
    }

    suspend fun fetchFarm(farmId: String): Farm?{
       val task =  firebaseFirestore.collection(FARMS_COLLECTION).document(farmId).get()
        val documentSnapShot = task.await()
        if (task.isSuccessful) {
            return documentSnapShot.toObject(Farm::class.java)
        }else {
            Throwable(task.exception)
            return null
        }
    }

    fun saveToPreferences(user:User,accessLevel: AccessLevel, farm : Farm){
        //is password reset
        preferencesRepo.saveData(
            IS_PASSWORD_RESET_KEY,
            user.passwordReset.toString()
        )

        //Farm Id
        preferencesRepo.saveData(
            FARM_ID_KEY,
            user.farmId
        )

        //user name
        preferencesRepo.saveData(
            USER_FIRST_NAME_KEY,
            user.firstName
        )
        preferencesRepo.saveData(
            USER_LAST_NAME_KEY,
            user.lastName
        )

        //user email
        preferencesRepo.saveData(
            USER_EMAIL_KEY,
            user.email
        )
        //user phone
        preferencesRepo.saveData(
            USER_PHONE_KEY,
            user.phone
        )
        //user AccessLevels
        preferencesRepo.saveData(
            EGG_COLLECTION_ACCESS,
            accessLevel.collectEggs.toString()
        )
        preferencesRepo.saveData(
            EDIT_HEN_COUNT_ACCESS,
            accessLevel.editHenCount.toString()
        )
        preferencesRepo.saveData(
            MANAGE_USERS_ACCESS,
            accessLevel.manageUsers.toString()
        )
        preferencesRepo.saveData(
            MANAGE_BLOCKS_CELLS_ACCESS,
            accessLevel.manageBlocksCells.toString()
        )

        //Farm Details
        preferencesRepo.saveData(
            FARM_NAME_KEY,
            farm.name
        )
        preferencesRepo.saveData(
            FARM_SUPER_USER_EMAIL,
            farm.superUserEmail
        )
        preferencesRepo.saveData(
            FARM_COUNTRY_KEY,
            farm.country
        )

        preferencesRepo.saveData(
            PAST_DAYS_KEY,
            "5"
        )
        preferencesRepo.saveData(
            CONSUCUTIVE_DAYS_KEY,
            "2"
        )

        preferencesRepo.saveData(
            THRESHOLD_RATIO_KEY,
            "0.5"
        )

        preferencesRepo.saveData(
            REPEAT_INTERVAL_KEY,
            "24"
        )
    }

    override suspend fun logIn(email: String, password: String): Result<Boolean> = coroutineScope {
        withContext(Dispatchers.IO){
            try {
                //first, sign in
                val authResult = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val firebaseUser = authResult.user ?: return@withContext Result.failure(Exception("Authentication failed"))

                //then fetch user details
                val user = fetchUserDetails(firebaseUser)?: return@withContext Result.failure(Exception("Failed to fetch user details"))

                //fetch access level
                val  accessLevel = fetchUserAccessLevel(firebaseUser.uid)?: return@withContext Result.failure(Exception("Failed to get access level"))

                //fetch farm
                val farm = fetchFarm(user.farmId)?: return@withContext Result.failure(Exception("Failed to get farm details"))

                //check if User's fetched farm is the same to previously logged in User's farm
                if (!isSimilarFarm(farm)){
                    //the farm is not similar
                }

                //store fetched data to shared preferences
                saveToPreferences( user, accessLevel, farm)

                Result.success(true)
            }catch (e:Exception){
                Result.failure(e)
            }
        }
    }

    private fun isSimilarFarm(farm: Farm) : Boolean{
        return farm.id == getFarmId()
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
    override suspend fun editFirstName(name: String): Result<Boolean> {
        val result = CompletableDeferred<Boolean>()
        firebaseFirestore.collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString())//firebaseAuth.uid.toString())
            .update("firstName", name)
            .addOnSuccessListener {
                result.complete(true)
                preferencesRepo.saveData(USER_FIRST_NAME_KEY, name)
            }
            .addOnFailureListener {
                result.completeExceptionally(it)
            }

        return if (result.await()) Result.success(true) else Result.failure(result.getCompletionExceptionOrNull()!!)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun editLastName(name: String): Result<Boolean> {
        val result = CompletableDeferred<Boolean>()
        firebaseFirestore.collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString())//firebaseAuth.uid.toString())
            .update("lastName", name)
            .addOnSuccessListener {
                result.complete(true)
                preferencesRepo.saveData(USER_FIRST_NAME_KEY, name)
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
                        preferencesRepo.saveData(USER_EMAIL_KEY, email)
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

    override suspend fun editFarmName(farmName: String): Result<Boolean> {
        val farmsCollection = firebaseFirestore.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())

        //update remote
        farmDocument.update("name", farmName)
            .addOnSuccessListener {
                preferencesRepo
                    .saveData(FARM_NAME_KEY, farmName)
            }


        return Result.success(true)

    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun editPhone(phone: String): Result<Boolean> {
        val completableDeferred = CompletableDeferred<Boolean>()
        firebaseFirestore.collection(USERS_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString())
            .update("phone", phone)
            .addOnSuccessListener {
                completableDeferred.complete(true)
                preferencesRepo.saveData(USER_PHONE_KEY, phone)
            }
            .addOnFailureListener {
                completableDeferred.completeExceptionally(it)
            }

        return if (completableDeferred.await()) Result.success(true) else Result.failure(
            completableDeferred.getCompletionExceptionOrNull()!!
        )
    }

    override suspend fun editAccessLevel(userId: String, accessLevel: AccessLevel): Result<Boolean> {
        val task  = firebaseFirestore.collection(USERS_COLLECTION).document(userId)
            .collection(ACCESS_LEVEL).document(userId + "accessLevel")
            .set(accessLevel)
         task.await()

        return if (task.isSuccessful) Result.success(true) else Result.failure(task.exception?.cause!!)
    }

    override suspend fun getFarmEmployees(farmId: String): Result<List<User>> {
        val listOfEmployees = mutableListOf<User>()
        var superUserEmail : String? = null

        try {
             val farm = firebaseFirestore.collection(FARMS_COLLECTION)
                .document(farmId)
                .get()
                 .await()
            val myFarm = farm.toObject<Farm>()
            myFarm?.let {
                superUserEmail = it.superUserEmail
            }

        }
        catch (e : Exception){
            Log.d("get employees", e.message.toString())
            return Result.failure(e)
        }

        return superUserEmail?.let {superEmail->
            try {
                val querySnapshot = firebaseFirestore.collection(USERS_COLLECTION)
                    .whereEqualTo("farmId", farmId)
                    .whereNotEqualTo("email", superEmail)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val user = document.toObject<User>()
                    user?.let {
                        listOfEmployees.add(it)
                    }
                }
                /*return*/
                Result.success(listOfEmployees)
            } catch (e: Exception) {
                Log.d("get employees", e.message.toString())
                /*return*/
                Result.failure(e)
            }
        }?: Result.success(listOfEmployees)
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
                    Log.d("Delete user", "successfully deleted on auth/Users")
                    //now delete the user form firestore records
                    firebaseFirestore.collection(USERS_COLLECTION)
                        .document(userId)
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Delete user", "successfully deleted on firestore/Users")
                            completableDeferred.complete(Result.success("User deleted successfully"))
                        }
                        .addOnFailureListener {
                            completableDeferred.complete(Result.failure(it))
                        }
                }
                .addOnFailureListener {
                    completableDeferred.complete(Result.failure(it))
                }
                .await()
        } ?: run {
            completableDeferred.complete(Result.failure(Throwable(message = "Deletion failed, Log into app afresh and try again")))
        }


        return completableDeferred.await()
    }

    override suspend fun getAccessLevel(userId: String): Result<AccessLevel> {
          return try {
              //var accessLevel = AccessLevel()
              val task  = firebaseFirestore.collection(USERS_COLLECTION).document(userId)
                  .collection(ACCESS_LEVEL).document(userId + "accessLevel")
                  .get()

              val docSnapShot = task.await()

              if (task.isSuccessful) {
                  val accessLevel = docSnapShot.toObject(AccessLevel::class.java)
                  if (accessLevel != null) {
                      Result.success(accessLevel)
                  } else {
                      Result.failure(Exception("AccessLevel is null"))
                  }
              } else {
                  Result.failure(task.exception ?: Exception("Unknown Firestore error"))
              }

        }catch (e : Exception){
            Result.failure(e)
        }
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!

}