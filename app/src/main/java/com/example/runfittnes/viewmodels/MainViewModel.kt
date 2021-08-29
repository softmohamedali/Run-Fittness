package com.example.runfittnes.viewmodels

import androidx.lifecycle.ViewModel
import com.example.runfittnes.data.entity.Run
import com.example.runfittnes.repo.DataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private var dataRepo: DataRepo
):ViewModel() {

    suspend fun insert()=dataRepo.dataBaseSource.insertRun(Run())

}