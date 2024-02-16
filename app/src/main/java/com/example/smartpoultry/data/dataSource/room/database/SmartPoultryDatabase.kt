package com.example.smartpoultry.data.dataSource.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartpoultry.data.dataSource.room.entities.PopulationChange.PopChange
import com.example.smartpoultry.data.dataSource.room.entities.PopulationChange.PopChangeDao
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.cells.CellsDao
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollectionDao
import com.example.smartpoultry.data.dataSource.room.entities.feeds.Feeds
import com.example.smartpoultry.data.dataSource.room.entities.feeds.FeedsDao
import com.example.smartpoultry.utils.DbConverters

@Database(
    entities = [EggCollection::class, Blocks::class, Cells::class, Feeds::class, PopChange::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(DbConverters::class)
abstract class SmartPoultryDatabase : RoomDatabase() {
    abstract fun eggCollectionDao(): EggCollectionDao
    abstract fun blocksDao() : BlocksDao
    abstract fun cellsDao() : CellsDao
    abstract fun feedsDao() : FeedsDao
    abstract fun popChangeDao() : PopChangeDao

    companion object{
        private  var INSTANCE : SmartPoultryDatabase? = null

        fun getInstance(context: Context) : SmartPoultryDatabase{
            synchronized(this){
                var instance  = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SmartPoultryDatabase::class.java,
                        "smart_poultry_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}