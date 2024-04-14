package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.USER_EMAIL_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_NAME_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_PHONE_KEY
import com.example.smartpoultry.data.dataSource.datastore.USER_ROLE_KEY
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
) : FirebaseAuthRepository{
    override suspend fun registerUser(email: String, password: String, role:String): Result<Boolean> = coroutineScope {

            val deferred = async(Dispatchers.IO) {
                try {
                    val authResult =
                        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                    val firebaseUser = authResult.user

                    firebaseUser?.let {
                        val user = User(email, role)
                        firebaseFirestore.collection("Users")
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
                        .collection("Users")
                        .document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener {documentSnapshot->
                            val user = documentSnapshot.toObject(User::class.java)

                            //save user details  to datastore

                            //user Role
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user->
                                    dataStore.saveData(USER_ROLE_KEY, user.role)
                                }
                            }

                            //User Name
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user->
                                    dataStore.saveData(USER_NAME_KEY, user.name)
                                }
                            }
                            //User email
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user->
                                    dataStore.saveData(USER_EMAIL_KEY, user.email)
                                }
                            }
                            //User phone
                            CoroutineScope(Dispatchers.IO).launch {
                                user?.let { user->
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

    override suspend fun resetPassword(email: String) : Result<Boolean> = coroutineScope {
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
        firebaseFirestore.collection("Users")
            .document(firebaseAuth.uid.toString())
            .update("name", name)
            .addOnSuccessListener {
               result.complete(true)
            }
            .addOnFailureListener{
               result.completeExceptionally(it)
            }
            //.await()

        return if (result.await()) Result.success(true) else Result.failure(result.getCompletionExceptionOrNull()!!)
    }

    override fun logOut() {
        firebaseAuth.signOut()
    }
}