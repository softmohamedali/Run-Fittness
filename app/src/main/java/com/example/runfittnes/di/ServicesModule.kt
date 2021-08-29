package com.example.runfittnes.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.runfittnes.R
import com.example.runfittnes.ui.MainActivity
import com.example.runfittnes.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServicesModule {

    @ServiceScoped
    @Provides
    fun provideFusedLocation(@ApplicationContext context: Context)=
            FusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
            @ApplicationContext context: Context,
            pendingIntent: PendingIntent
    )= NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentText("Run")
            .setContentTitle("Run")
            .setSmallIcon(R.drawable.ic_run)
            .setContentIntent(pendingIntent)


    @ServiceScoped
    @Provides
    fun providePendingIntent(
            @ApplicationContext context: Context,
    )= PendingIntent.getActivity(
            context,
            1,
            Intent(context, MainActivity::class.java).also {
                it.action=Constants.ACTION_PENDING_TO_TRCKFRAG
            },
            PendingIntent.FLAG_UPDATE_CURRENT
    )
}