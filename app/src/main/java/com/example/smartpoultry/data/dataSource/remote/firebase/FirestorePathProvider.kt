package com.example.smartpoultry.data.dataSource.remote.firebase

import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirestorePathProvider @Inject constructor(
    private val dataStore: AppDataStore,
    private val firestoreDb: FirebaseFirestore
) {
    val farmsCollection = firestoreDb.collection("Farms")
    var farmDocument : DocumentReference? = null
    var blocksCollection: CollectionReference? = null
    var cellsCollection : CollectionReference? = null

    init{
        CoroutineScope(Dispatchers.IO).launch{
            dataStore.readData(FARM_ID_KEY).filter { it.isNotBlank() }.collect{id->
                farmDocument = farmsCollection.document(id)
                blocksCollection = farmDocument?.collection("Blocks")
                cellsCollection = farmDocument?.collection("Cells")
            }
        }
    }
}