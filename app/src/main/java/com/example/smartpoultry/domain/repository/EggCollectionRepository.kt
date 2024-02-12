package com.example.smartpoultry.domain.repository

interface EggCollectionRepository{
    suspend fun addNewRecord()
}