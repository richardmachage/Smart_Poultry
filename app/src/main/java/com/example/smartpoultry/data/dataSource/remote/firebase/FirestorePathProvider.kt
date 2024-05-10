package com.example.smartpoultry.data.dataSource.remote.firebase

import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePathProvider @Inject constructor(
    private val dataStore: AppDataStore,
    private val firestoreDb: FirebaseFirestore,
    private val preferencesRepo: PreferencesRepo,
    private val firebaseAuth: FirebaseAuth
) {
    lateinit var farmId: String
    lateinit var  farmCollection : CollectionReference//= firestoreDb.collection("Farms")

    lateinit var farmDocRef : DocumentReference//= farmCollection.document(farmId!!)

    lateinit var blocksCollection : CollectionReference//= farmDocRef.collection("Blocks")



    suspend fun getFarmIdFromFirebase(): String {
        val user = User()
        firestoreDb.collection("Users")
            .document(firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val user1 = it.toObject(User::class.java)
                user.farmId = user1?.farmId.toString()
            }
            .await()
        return user.farmId
    }

    suspend fun initializeFarm(): String = coroutineScope {
        val deferred = async(Dispatchers.IO) {
            return@async preferencesRepo.loadData(FARM_ID_KEY) ?: "no data"
        }
        deferred.await()
    }

    /*val farmCollection = firestoreDb.collection("Farms")

    val farmDocRef = farmCollection.document(farmId!!)

    val blocksCollection = farmDocRef.collection("Blocks")

    val cellsCollection = farmDocRef.collection("Cells")*/
    /*private val farmIDFlow = dataStore.readData(FARM_ID_KEY).filter { it.isNotEmpty() }.distinctUntilChanged()

    val farmDocumentFlow: Flow<DocumentReference> = farmIDFlow.map { farmId->
        firestoreDb.collection("Farms").document(farmId)
    }

    val blocksCollectionFlow: Flow<CollectionReference> = farmDocumentFlow.map { farmDocument ->
        farmDocument.collection("Blocks")
    }

    val cellsCollectionFlow: Flow<CollectionReference> = farmDocumentFlow.map { farmDocument ->
        farmDocument.collection("Cells")
    }*/
}