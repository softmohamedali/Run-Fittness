package com.example.runfittnes.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.runfittnes.data.entity.Run
import com.example.runfittnes.repo.DataRepo
import com.example.runfittnes.utils.SortedBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var dataRepo: DataRepo
):ViewModel() {

    var runsbyData=dataRepo.dataBaseSource.getAllRun().asLiveData()
    var runsbyAvgSpeed=dataRepo.dataBaseSource.getAllRunSortedByAvgSpeed().asLiveData()
    var runsbyDistence=dataRepo.dataBaseSource.getAllRunSortedByDistance().asLiveData()
    var runsbyBurnedCal=dataRepo.dataBaseSource.getAllRunSortedByCaloryBurned().asLiveData()
    var runsbyTimeRun=dataRepo.dataBaseSource.getAllRunSortedByRunTime().asLiveData()

    var runs=MediatorLiveData<List<Run>>()
    var sortedBy=SortedBy.DATA

    init {
        runs.addSource(runsbyData){
            if (sortedBy==SortedBy.DATA)
            {
                it?.let { runs.value=it }
            }
        }
        runs.addSource(runsbyAvgSpeed){
            if (sortedBy==SortedBy.AVGSPEED)
            {
                it?.let { runs.value=it }
            }
        }
        runs.addSource(runsbyDistence){
            if (sortedBy==SortedBy.DISTENCE)
            {
                it?.let { runs.value=it }
            }
        }
        runs.addSource(runsbyBurnedCal){
            if (sortedBy==SortedBy.CALBURNED)
            {
                it?.let { runs.value=it }
            }
        }
        runs.addSource(runsbyTimeRun){
            if (sortedBy==SortedBy.TIMERUN)
            {
                it?.let { runs.value=it }
            }
        }
    }

    fun sortBy(sortBy: SortedBy) = when(sortedBy){
        SortedBy.TIMERUN ->{runsbyTimeRun.value?.let { runs.value=it }}
        SortedBy.DATA ->{runsbyData.value?.let { runs.value=it }}
        SortedBy.CALBURNED ->{runsbyBurnedCal.value?.let { runs.value=it }}
        SortedBy.AVGSPEED ->{runsbyAvgSpeed.value?.let { runs.value=it }}
        SortedBy.DISTENCE ->{runsbyDistence.value?.let { runs.value=it }}
    }.also { sortedBy=sortBy}

    fun insertRun(run: Run) {
        viewModelScope.launch {
            dataRepo.dataBaseSource.insertRun(run)
        }
    }

}