package com.example.runfittnes.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.runfittnes.R
import com.example.runfittnes.ui.MainActivity
import com.example.runfittnes.utils.Constants
import com.example.runfittnes.utils.MyUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class TrackServices:LifecycleService() {

    private var isFirst=true

    private var isTrackingKiller=false

    @Inject
    lateinit var fusedLocation:FusedLocationProviderClient

    @Inject
    lateinit var baseNotification:NotificationCompat.Builder

    lateinit var curNotification:NotificationCompat.Builder

    val timeInSeacound=MutableLiveData<Long>()

    private var timeStart=0L
    private var timeLab=0L
    private var timeRun=0L
    private var isTimeEnable=false
    private var lastTimeSecound=0L


    companion object{
        val isTracking=MutableLiveData<Boolean>()
        val trackPath=MutableLiveData<MutableList<MutableList<LatLng>>>()
        val timeInMilles=MutableLiveData<Long>()
    }

    private fun initValues(){
        isTracking.postValue(false)
        trackPath.postValue(mutableListOf())
        timeInSeacound.postValue(0L)
        timeInMilles.postValue(0L)
    }

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        curNotification=baseNotification
        initValues()

        isTracking.observe(this,{
            upDateLocation(it)
            updateNotificationContent(it)
        })

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("debug","START_OR_RESUME")
        intent?.let {
            when(it.action){
                Constants.SERVICES_ACTION_START_OR_RESUME ->{
                    if (isFirst){
                        startForegroudServices()
                        isFirst=false
                    }else
                    {
                        startTimeTrack()
                        Log.d("debug","START_OR_RESUME")
                    }

                }
                Constants.SERVICES_ACTION_PAUSE ->{
                    Log.d("debug","Pause")
                    pausetForegroudServices()
                }
                Constants.SERVICES_ACTION_STOP ->{
                    Log.d("debug","stop")
                    trackKilller()
                }
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun trackKilller()
    {
        isFirst=true
        isTrackingKiller=true
        initValues()
        pausetForegroudServices()
        stopForeground(true)
        stopSelf()
    }

    private fun startTimeTrack()
    {
        addEmptyPolyline()
        isTracking.postValue(true)
        isTimeEnable=true
        timeStart=System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!)
            {
                timeLab=(System.currentTimeMillis())-timeStart

                timeInMilles.postValue(timeRun+timeLab)
                if (timeInMilles.value!! > lastTimeSecound+1000L)
                {
                    timeInSeacound.postValue(timeInSeacound.value!!+1L)
                    lastTimeSecound+=1000L
                }
                delay(50L)
            }
            timeRun+=timeLab
        }
    }

    private fun updateNotificationContent(isTracking:Boolean)
    {
        val notificationTextContent=if (isTracking) "Pause" else "Resume"
        val pendingIntent=if (isTracking)
        {
            val sendPauseIntent=Intent(this,TrackServices::class.java).apply {
                action=Constants.SERVICES_ACTION_PAUSE
            }
            PendingIntent.getService(this,3,sendPauseIntent, FLAG_UPDATE_CURRENT)

        }else {
            val sendResumeIntent=Intent(this,TrackServices::class.java).apply {
                action=Constants.SERVICES_ACTION_START_OR_RESUME
            }
            PendingIntent.getService(this,4,sendResumeIntent, FLAG_UPDATE_CURRENT)
        }
        val notificationManger=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotification.javaClass.getDeclaredField("mActions").apply {
            isAccessible=true
            set(curNotification,ArrayList<NotificationCompat.Action>())
        }
        if (!isTrackingKiller)
        {
            curNotification=baseNotification.addAction(
                    R.drawable.ic_run,
                    notificationTextContent,
                    pendingIntent
            )
            notificationManger.notify(Constants.NOTIFICATION_ID,curNotification.build())
        }
    }

    /*
    *this fun will call when we need to add an empty path track
    * and when we will do this when we pause tracking and already wallk
    * but not tracking
     */
    private fun addEmptyPolyline()=trackPath.value?.apply {
            add(mutableListOf())
            trackPath.value=this
        } ?: trackPath.postValue(mutableListOf(mutableListOf()))

    private fun addlocationToPathTrack(location:Location?){
        trackPath.value?.apply {
            last().add(LatLng(location!!.latitude,location.longitude))
            trackPath.postValue(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun upDateLocation(isTracking:Boolean){
        if (isTracking)
        {
            if (MyUtility.hasRequirePermission(this))
            {
                val locationRequest=LocationRequest().apply {
                    interval=6000L
                    fastestInterval=2000L
                    priority=LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocation.requestLocationUpdates(
                    locationRequest,
                    trackLocationCallBack,
                    Looper.getMainLooper()
                )
            }
        }else{
            fusedLocation.removeLocationUpdates(trackLocationCallBack)
        }
    }

    val trackLocationCallBack=object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            if (isTracking.value!!)
            {
                p0.let {
                    for (location in it.locations){
                        addlocationToPathTrack(location)
                        Log.d("location","${location.latitude} ${location.longitude}")
                    }
                }
            }
        }


    }

    private fun startForegroudServices(){
        startTimeTrack()
        isTracking.postValue(true)
        showAndSetupNavigationComponent()



    }
    ///try somthing here ===>delte isTimeEnable=false DGBNXGNDFGNFHNDHNDFH&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    private fun pausetForegroudServices(){
        isTracking.postValue(false)
        isTimeEnable=false

    }

    private fun showAndSetupNavigationComponent(){
        val notificationManger=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createNotificationChannel(notificationManger)
        }

        startForeground(Constants.NOTIFICATION_ID,baseNotification.build())
        timeInSeacound.observe(this,{
            if (!isTrackingKiller)
            {
                notificationManger.notify(
                        Constants.NOTIFICATION_ID,
                        curNotification.setContentText(MyUtility.formatTime(it*1000)).build())
            }
        })

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManger:NotificationManager)
    {
        val notificationChannel=NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManger.createNotificationChannel(notificationChannel)


    }
}