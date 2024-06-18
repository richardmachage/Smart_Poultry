package com.example.smartpoultry.data.dataSource.datastore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


//@Singleton
class AppDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val fireStoreDB: FirebaseFirestore,
    private val fireBaseAuth: FirebaseAuth,
) {

    var farmID by mutableStateOf("")

    init {
        //listenForFireStoreChanges()
        /*CoroutineScope(Dispatchers.IO).launch {
            readData(FARM_ID_KEY).collect {theId->
                withContext(Dispatchers.Main){
                    farmID = theId
                }
            }
        }*/
    }


   /* private fun listenForFireStoreChanges() {
        fireStoreDB.collection(USERS_COLLECTION)
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

                    //is password reset
                    CoroutineScope(Dispatchers.IO).launch {
                        user?.let {
                            saveData(IS_PASSWORD_RESET_KEY, user.passwordReset.toString())
                        }
                    }
                }

            }
    }
*/
   /* suspend fun saveData(key: String, value: String) {
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

    suspend  fun deleteData(key: String){
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {
            it.remove(dataStoreKey)
        }
    }
*/
    suspend fun clearDataStore() {
        dataStore.edit { myPreferences ->
            myPreferences.clear()
        }
    }

}