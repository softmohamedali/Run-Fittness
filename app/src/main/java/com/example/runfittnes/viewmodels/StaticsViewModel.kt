package com.example.runfittnes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.runfittnes.data.entity.Run
import com.example.runfittnes.repo.DataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.sample
import javax.inject.Inject

@HiltViewModel
class StaticsViewModel @Inject constructor(
    private var dataRepo: DataRepo
): ViewModel() {

    val totalAvgSpeed=dataRepo.dataBaseSource.getSumAvgSpeed().asLiveData()
    val totalCalBurned=dataRepo.dataBaseSource.getSumcaloryBurned().asLiveData()
    val totalDistence=dataRepo.dataBaseSource.getSumDistance().asLiveData()
    val totalRunTime=dataRepo.dataBaseSource.getSumRunTime().asLiveData()
}