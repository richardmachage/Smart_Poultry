package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : FirebaseAuthRepository{
    override fun registerUser(email: String, password: String, role:String): Boolean {
        var isSuccess = false
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {authResult ->
                val firebaseUser = authResult.user
                val user = User(email, role)

                //add the user info to fireStore Users collection
                firebaseUser?.let {
                    firebaseFirestore.collection("Users")
                        .document(it.uid)
                        .set(user)
                        .addOnSuccessListener {
                            isSuccess = true
                        }
                }
            }

        return isSuccess
    }

    override fun logIn(email: String, password: String): Boolean {
        var isSuccess = false
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { isSuccess = true }
        return isSuccess
    }
}