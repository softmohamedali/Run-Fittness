package com.example.runfittnes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.runfittnes.data.entity.Run
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("DELETE FROM runtable")
    suspend fun deleteAllRun()

    @Query("SELECT * FROM runtable ORDER BY timeStart DESC")
    fun getAllRun():Flow<List<Run>>

    @Query("SELECT * FROM runtable ORDER BY avgSpeed DESC")
    fun getAllRunSortedByAvgSpeed():Flow<List<Run>>

    @Query("SELECT * FROM runtable ORDER BY distence DESC")
    fun getAllRunSortedByDistance():Flow<List<Run>>

    @Query("SELECT * FROM runtable ORDER BY caloryBurned DESC")
    fun getAllRunSortedByCaloryBurned():Flow<List<Run>>

    @Query("SELECT * FROM runtable ORDER BY runTime DESC")
    fun getAllRunSortedByRunTime():Flow<List<Run>>

    @Query("SELECT SUM(distence) FROM runtable")
    fun getSumDistance():Flow<Int>

    @Query("SELECT SUM(caloryBurned) FROM runtable")
    fun getSumcaloryBurned():Flow<Int>

    @Query("SELECT AVG(avgSpeed) FROM runtable")
    fun getSumAvgSpeed():Flow<Float>

    @Query("SELECT SUM(runTime) FROM runtable")
    fun getSumRunTime():Flow<Long>

}

















