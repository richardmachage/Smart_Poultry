package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : FirebaseAuthRepository{
    override fun registerUser(email: String, password: String): Boolean {
        var result = false

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = it.user
            }


        return result
    }

    override fun logIn(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }
}