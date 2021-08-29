package com.example.runfittnes.repo

import com.example.runfittnes.data.Dao
import com.example.runfittnes.data.RunDataBase
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DataRepo @Inject constructor(
   var dataBaseSource:Dao
) {


}