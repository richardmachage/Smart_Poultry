package com.example.smartpoultry.data.dataSource.room.database

import androidx.room.Database
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
    version = 1
)

@TypeConverters(DbConverters::class)
abstract class SmartPoultryDatabase : RoomDatabase() {
    abstract val eggCollectionDao: EggCollectionDao
    abstract val blocksDao : BlocksDao
    abstract val cellsDao : CellsDao
    abstract val feedsDao : FeedsDao
    abstract val popChangeDao : PopChangeDao
}