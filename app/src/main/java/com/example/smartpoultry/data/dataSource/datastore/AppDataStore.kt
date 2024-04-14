package com.example.smartpoultry.data.dataSource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val USER_ROLE_KEY = "user_role"
const val USER_NAME_KEY = "user_name"
const val USER_PHONE_KEY = "user_phone"
const val USER_EMAIL_KEY = "user_email"

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

    fun readData(key : String) : Flow<String>{
        val dataStoreKey = stringPreferencesKey(key)

        return dataStore.data.map {preferences->
            preferences[dataStoreKey] ?: ""
        }
    }

    suspend fun clearDataStore(){
        dataStore.edit {myPreferences->
            myPreferences.clear()
        }
    }

}