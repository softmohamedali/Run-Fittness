package com.example.runfittnes.viewmodels

import androidx.lifecycle.ViewModel
import com.example.runfittnes.data.entity.Run
import com.example.runfittnes.repo.DataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StaticsViewModel @Inject constructor(
    private var dataRepo: DataRepo
): ViewModel() {


}