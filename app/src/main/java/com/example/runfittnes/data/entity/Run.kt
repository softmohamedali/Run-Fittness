package com.example.runfittnes.data.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runfittnes.utils.Constants

@Entity(tableName = Constants.RUN_TABLE_NAME)
data class Run(
    var img:Bitmap?=null,
    var timeStart:Long?=0L,
    var avgSpeed:Float?=0F,
    var distence:Int?=0,
    var runTime:Long?=0L,
    var caloryBurned:Int?=0
){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}