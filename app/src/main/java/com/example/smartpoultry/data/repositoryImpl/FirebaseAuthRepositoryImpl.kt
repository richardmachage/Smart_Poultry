package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
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
        /*
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result.user
                        val user = User(email, role)

                        //add the user info to fireStore Users collection
                        firebaseUser?.let {
                            firebaseFirestore.collection("Users")
                                .document(it.uid)
                                .set(user)
                                .addOnSuccessListener {
                                }
                        }

                    } else {
                        Log.i("Error firebaseAuth: ", task.exception?.message.toString())
                    }
                }
            return
        }
    }*/

    override fun logIn(email: String, password: String): Boolean {
        var isSuccess = false
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { isSuccess = true }
        return isSuccess
    }

    override fun resetPassword(email: String): Boolean {
        var isSuccessful = false
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                isSuccessful = true
            }
        return isSuccessful
    }
}