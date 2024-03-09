package com.example.smartpoultry.data.dataSource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
}