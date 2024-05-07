package com.example.smartpoultry.data.dataSource.remote.firebase

import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirestorePathProvider @Inject constructor(
    private val dataStore: AppDataStore,
    private val firestoreDb: FirebaseFirestore
) {
    private val farmIDFlow = dataStore.readData(FARM_ID_KEY).filter { it.isNotEmpty() }.distinctUntilChanged()

    val farmDocumentFlow: Flow<DocumentReference> = farmIDFlow.map { farmId->
        firestoreDb.collection("Farms").document(farmId)
    }

    val blocksCollectionFlow: Flow<CollectionReference> = farmDocumentFlow.map { farmDocument ->
        farmDocument.collection("Blocks")
    }

    val cellsCollectionFlow: Flow<CollectionReference> = farmDocumentFlow.map { farmDocument ->
        farmDocument.collection("Cells")
    }
}