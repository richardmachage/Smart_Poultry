package com.example.smartpoultry.data.dataSource.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollectionDao

@Database(
    entities = [EggCollection::class],
    version = 1
)
abstract class SmartPoultryDatabase : RoomDatabase() {
    abstract val dao: EggCollectionDao
}