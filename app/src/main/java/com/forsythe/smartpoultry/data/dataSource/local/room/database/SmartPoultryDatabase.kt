package com.forsythe.smartpoultry.data.dataSource.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.PopulationChange.PopChange
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.PopulationChange.PopChangeDao
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.alerts.Alerts
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.alerts.AlertsDao
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks.Blocks
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks.BlocksDao
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.CellsDao
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollectionDao
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.feeds.FeedTrack
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.feeds.Feeds
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.feeds.FeedsDao
import com.forsythe.smartpoultry.utils.DbConverters

@Database(
    entities = [EggCollection::class, Blocks::class, Cells::class, Feeds::class, PopChange::class, FeedTrack::class, Alerts::class],
    version = 6,
    exportSchema = false
)

@TypeConverters(DbConverters::class)
abstract class SmartPoultryDatabase : RoomDatabase() {
    abstract fun eggCollectionDao(): EggCollectionDao
    abstract fun blocksDao() : BlocksDao
    abstract fun cellsDao() : CellsDao
    abstract fun feedsDao() : FeedsDao
    abstract fun popChangeDao() : PopChangeDao
    abstract fun alertsDao() : AlertsDao

    companion object{
        private  var INSTANCE : SmartPoultryDatabase? = null

        fun getInstance(context: Context) : SmartPoultryDatabase {
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