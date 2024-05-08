package com.example.smartpoultry.data.dataSource.remote.firebase

import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestorePathProvider @Inject constructor(
    private val dataStore: AppDataStore,
    private val firestoreDb: FirebaseFirestore,
    preferencesRepo: PreferencesRepo
) {

    val farmId = preferencesRepo.loadData(FARM_ID_KEY)?:""

    val farmCollection = firestoreDb.collection("Farms")

    val farmDocRef = farmCollection.document(farmId)

    val blocksCollection = farmDocRef.collection("Blocks")

    val cellsCollection =farmDocRef.collection("Cells")
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