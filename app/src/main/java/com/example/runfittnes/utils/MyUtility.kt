package com.example.runfittnes.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

object MyUtility {

    fun hasRequirePermission(context:Context)=
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)
            {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }else
            {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }

    ///try somthing here ===>var msDGBNXGNDFGNFHNDHNDFH&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    fun formatTime(mls:Long,enableMs:Boolean=false):String
    {
        var ms=mls
        var hour=TimeUnit.MILLISECONDS.toHours(ms)
        ms-=TimeUnit.HOURS.toMillis(hour)
        var men=TimeUnit.MILLISECONDS.toMinutes(ms)
        ms-=TimeUnit.MINUTES.toMillis(men)
        var sec=TimeUnit.MILLISECONDS.toSeconds(ms)

        if (!enableMs)
        {
            return "${if (hour<10) "0" else ""}${hour}:"+
                    "${if (men<10) "0" else ""}${men}:"+
                    "${if (sec<10) "0" else ""}${sec}"
        }
        ms-=TimeUnit.SECONDS.toMillis(sec)
        ms /=100
        return "${if (hour<10) "0" else ""}${hour}:"+
                "${if (men<10) "0" else ""}${men}:"+
                "${if (sec<10) "0" else ""}${sec}:"+
                "${if (ms<10) "0" else ""}${ms}"
    }

}