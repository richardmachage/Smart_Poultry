package com.example.smartpoultry.data.dataSource.remote.firebase

import android.util.Log
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlocksCollectionListener @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val blocksDao: BlocksDao
){
    init {
        listenForFireStoreChanges()
    }


    private fun listenForFireStoreChanges() {
        fireStore.collection("Blocks").addSnapshotListener{
            querySnapshot, exception ->

            if (exception != null){ //f an error exists, it logs the error and returns early from the listener.
                Log.w("Error","Listen failed.", exception)

            }


            for (docChange in querySnapshot!!.documentChanges){
                val block = docChange.document.toObject<Blocks>() //For each change, it converts the document to a Blocks object

                when (docChange.type){
                    DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED -> updateLocalDatabase(block)
                    DocumentChange.Type.REMOVED -> deleteFromLocalDatabase(block)
                }
            }
        }
    }

    private fun deleteFromLocalDatabase(block: Blocks) {
        CoroutineScope(Dispatchers.IO).launch {
            blocksDao.deleteBlock(block)
        }
    }

    private fun updateLocalDatabase(block: Blocks) {
        CoroutineScope(Dispatchers.IO).launch {
            blocksDao.addNewBlock(block)
        }
    }
}