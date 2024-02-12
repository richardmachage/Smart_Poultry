package com.example.smartpoultry.domain.repository

import com.example.smartpoultry.domain.domainModels.Block

interface BlocksRepository {
    suspend fun addNewBlock(block: Block)
    suspend fun deleteBlock(block: Block)


}