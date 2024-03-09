package com.example.smartpoultry.data.dataSource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

class AppDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

}