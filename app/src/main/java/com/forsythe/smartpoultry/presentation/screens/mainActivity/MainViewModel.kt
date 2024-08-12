package com.forsythe.smartpoultry.presentation.screens.mainActivity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.domain.repository.BlocksRepository
import com.forsythe.smartpoultry.domain.repository.CellsRepository
import com.forsythe.smartpoultry.domain.repository.EggCollectionRepository
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.forsythe.smartpoultry.utils.FIRST_INSTALL
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
   // val dataStore: AppDataStore,
    val preferencesRepo: PreferencesRepo,
    val blocksRepository: BlocksRepository,
    val cellsRepository: CellsRepository,
    val eggCollectionRepository: EggCollectionRepository
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var isFirstInstall by mutableStateOf(false)

    init {
        checkFirstInstall()
        checkIfLoggedIn()
    // getThisUser(preferencesRepo)
    //checkIfFarmSaved()
    }


    private fun checkIfLoggedIn() {
        firebaseAuth.currentUser?.let {
            isLoggedIn = true
            //val farmId = getFarmId()

            blocksRepository.listenForFireStoreChanges()
            cellsRepository.listenForFireStoreChanges()
            eggCollectionRepository.listenForFireStoreChanges()
            //preferencesRepo.listenForFirestoreChanges(firebaseAuth.currentUser?.uid.toString())
        }
    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!
    private fun checkFirstInstall() {
        /*viewModelScope.launch {
            dataStore.readData(FIRST_INSTALL)
                .collect { if (it != "onBoardingDone") isFirstInstall = true }
        }*/
        val firstInstall = preferencesRepo.loadData(FIRST_INSTALL)!!
        if (firstInstall != "onBoardingDone") isFirstInstall = true


    }

    /*private fun checkIfFarmSaved() {
        viewModelScope.launch {
            dataStore.readData(FARM_ID_KEY).collect {
                // Log.d("Farm","Farm id from datastore :$it")
            }
        }
    }*/

}