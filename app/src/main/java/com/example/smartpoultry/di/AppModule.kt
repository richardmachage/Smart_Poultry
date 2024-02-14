package com.example.smartpoultry.di

import android.app.Application
import com.example.smartpoultry.data.dataSource.room.database.SmartPoultryDatabase
import com.example.smartpoultry.data.repositoryImpl.BlocksRepositoryImpl
import com.example.smartpoultry.domain.repository.BlocksRepository
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


}