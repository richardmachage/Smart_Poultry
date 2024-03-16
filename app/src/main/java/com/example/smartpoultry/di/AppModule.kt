package com.example.smartpoultry.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.room.database.SmartPoultryDatabase
import com.example.smartpoultry.data.repositoryImpl.BlocksRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.CellsRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.EggCollectionRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.FeedsRepositoryImpl
import com.example.smartpoultry.data.repositoryImpl.FirebaseAuthRepositoryImpl
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.domain.repository.FeedsRepository
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.domain.trendAnalysis.TrendAnalysis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesFirebaseFireStoreInstance(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun providesBlocksRepository(database: SmartPoultryDatabase, firestore: FirebaseFirestore): BlocksRepository{
        return BlocksRepositoryImpl(database.blocksDao(), firestore)
    }

    @Provides
    @Singleton
    fun providesEggCollectionRepository(database: SmartPoultryDatabase, fireStoreDb: FirebaseFirestore): EggCollectionRepository{
        return EggCollectionRepositoryImpl(database.eggCollectionDao(), fireStoreDb = fireStoreDb)
    }

    @Provides
    @Singleton
    fun providesCellsRepository(database: SmartPoultryDatabase, fireStoreDb: FirebaseFirestore): CellsRepository{
        return CellsRepositoryImpl(cellsDao = database.cellsDao(), fireStoreDb = fireStoreDb)
    }

    @Provides
    @Singleton
    fun provideFeedsRepository(database: SmartPoultryDatabase): FeedsRepository{
        return FeedsRepositoryImpl(database.feedsDao())
    }

    //dataStore
    @Provides
    @Singleton
    fun providePreferenceDataStore(@ApplicationContext context : Context) : DataStore<Preferences>{
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("my_preferences")
        }
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuthRepository(firebaseAuth: FirebaseAuth, fireStoreDb: FirebaseFirestore, dataStore: AppDataStore) : FirebaseAuthRepository{
        return FirebaseAuthRepositoryImpl(firebaseAuth = firebaseAuth, firebaseFirestore = fireStoreDb, dataStore= dataStore)
    }

    @Provides
    @Singleton
    fun providesTrendAnalysis(eggCollectionRepository: EggCollectionRepository, cellsRepository: CellsRepository,dataStore: AppDataStore) : TrendAnalysis{
        return TrendAnalysis(eggCollectionRepository,cellsRepository, dataStore)
    }
}