package com.example.runfittnes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runfittnes.data.entity.Run

@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(TypeConverter::class)
abstract class RunDataBase:RoomDatabase() {
    abstract fun getDao():Dao
}