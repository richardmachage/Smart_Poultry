package com.example.smartpoultry.activity

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.remote.firebase.BlocksCollectionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val blocksCollectionListener : BlocksCollectionListener,
    ): ViewModel() {

}