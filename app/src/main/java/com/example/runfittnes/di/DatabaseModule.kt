package com.example.runfittnes.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.runfittnes.data.RunDataBase
import com.example.runfittnes.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRunDatabase(@ApplicationContext context: Context)=
        Room.databaseBuilder(
            context,
            RunDataBase::class.java,
            Constants.RUN_DB_NAME
        ).build()

    @Provides
    @Singleton
    fun provideRunDao(runDataBase: RunDataBase)=
        runDataBase.getDao()
}