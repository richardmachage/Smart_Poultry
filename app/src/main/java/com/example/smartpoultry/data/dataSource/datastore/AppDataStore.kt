package com.example.smartpoultry.data.dataSource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveData (key : String , value : String){
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit {myPreferences->
            myPreferences[dataStoreKey] = value
        }
    }

    suspend fun readData(key : String) : Flow<String>{
        val dataStoreKey = stringPreferencesKey(key)

        return dataStore.data.map {preferences->
            preferences[dataStoreKey] ?: ""
        }
    }

    /*If you need a single snapshot of the data instead of an ongoing observation,
    consider accessing the Flow at the point of use, where you can handle the asynchronous nature more appropriately,
    for example:
    lifecycleScope.launch {
        val singleSnapshot = appDataStore.readData("myKey").first()
        // Use singleSnapshot here
    }*/
}