package com.example.smartpoultry.data.dataSource.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

const val USER_ROLE_KEY = "user_role"
const val USER_NAME_KEY = "user_name"
const val USER_PHONE_KEY = "user_phone"
const val USER_EMAIL_KEY = "user_email"

@Singleton
class AppDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val fireStoreDB: FirebaseFirestore,
    private val fireBaseAuth: FirebaseAuth
) {

    init {
        listenForFireStoreChanges()
    }

    private fun listenForFireStoreChanges() {
        fireStoreDB.collection("Users")
            .document(fireBaseAuth.currentUser?.uid.toString())
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w("Error", "Listen failed.", exception)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    //role
                    CoroutineScope(Dispatchers.IO).launch {
                            user?.let {
                                saveData(key = USER_ROLE_KEY, value = it.role)
                            }
                    }

                    //name
                    CoroutineScope(Dispatchers.IO).launch {
                        user?.let {
                            saveData(key = USER_NAME_KEY, value = it.name)
                        }
                    }
                    //email
                    CoroutineScope(Dispatchers.IO).launch {
                        user?.let {
                            saveData(key = USER_EMAIL_KEY, value = it.email)
                        }
                    }
                    //phone
                    CoroutineScope(Dispatchers.IO).launch {
                        user?.let {
                            saveData(key = USER_PHONE_KEY, value = it.phone)
                        }
                    }
                }

            }
    }

    suspend fun saveData(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { myPreferences ->
            myPreferences[dataStoreKey] = value
        }
    }

    fun readData(key: String): Flow<String> {
        val dataStoreKey = stringPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: ""
        }
    }

    suspend fun clearDataStore() {
        dataStore.edit { myPreferences ->
            myPreferences.clear()
        }
    }

}