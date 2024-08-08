package com.forsythe.smartpoultry.data.dataSource.local.datastore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.utils.ACCESS_LEVEL
import com.forsythe.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.forsythe.smartpoultry.utils.EGG_COLLECTION_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_USERS_ACCESS
import com.forsythe.smartpoultry.utils.USERS_COLLECTION
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesRepo @Inject constructor(
    @ApplicationContext context: Context,
    private val firestore: FirebaseFirestore,
    //private val firebaseAuth: FirebaseAuth,
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_FILE_KEY, Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFERENCES_FILE_KEY = "com.forsythe.smartpoultry.PREFERENCES"
    }

    fun listenForFirestoreChanges(userId: String) {
        firestore.collection(USERS_COLLECTION).document(userId).collection(ACCESS_LEVEL)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) { //if an error exists, it logs the error and returns early from the listener.
                    Log.w(
                        "Preferences Repo",
                        "Listening to Firestore changes failed.",
                        exception
                    )
                    return@addSnapshotListener
                }

                for (docChange in querySnapshot!!.documentChanges) {
                    Log.d(
                        "Preferences Repo",
                        "Listening to Firestore changes..."
                    )
                    val accessLevel = docChange.document.toObject(AccessLevel::class.java)

                    when (docChange.type) {
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED, DocumentChange.Type.REMOVED -> {
                            saveData(
                                MANAGE_BLOCKS_CELLS_ACCESS,
                                accessLevel.manageBlocksCells.toString()
                            )
                            saveData(EDIT_HEN_COUNT_ACCESS, accessLevel.editHenCount.toString())
                            saveData(MANAGE_USERS_ACCESS, accessLevel.manageUsers.toString())
                            saveData(EGG_COLLECTION_ACCESS, accessLevel.collectEggs.toString())
                        }
                    }
                }
            }
    }

    fun saveData(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun loadData(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun deleteData(key: String) {
        sharedPreferences
            .edit()
            .remove(key)
            .commit()
    }


}