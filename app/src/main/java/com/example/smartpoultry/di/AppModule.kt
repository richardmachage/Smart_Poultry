package com.example.smartpoultry.di

import android.app.Application
import com.example.smartpoultry.data.dataSource.room.database.SmartPoultryDatabase
import com.example.smartpoultry.data.repositoryImpl.BlocksRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.CellsRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.EggCollectionRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.FeedsRepositoryImpl
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.domain.repository.FeedsRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesSmartPoultryDatabase(application : Application):SmartPoultryDatabase{
        return SmartPoultryDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun providesBlocksRepository(database: SmartPoultryDatabase): BlocksRepository{
        return BlocksRepositoryImpl(database.blocksDao())
    }

    @Provides
    @Singleton
    fun providesEggCollectionRepository(database: SmartPoultryDatabase): EggCollectionRepository{
        return EggCollectionRepositoryImpl(database.eggCollectionDao())
    }

    @Provides
    @Singleton
    fun providesCellsRepository(database: SmartPoultryDatabase): CellsRepository{
        return CellsRepositoryImpl(database.cellsDao())
    }

    @Provides
    @Singleton
    fun provideFeedsRepository(database: SmartPoultryDatabase): FeedsRepository{
        return FeedsRepositoryImpl(database.feedsDao())
    }

    @Provides
    @Singleton
    fun providesFirebaseFireStoreInstance(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}