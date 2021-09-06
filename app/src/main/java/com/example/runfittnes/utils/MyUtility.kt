package com.example.runfittnes.utils

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
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
        ms /=1000
        return "${if (hour<10) "0" else ""}${hour}:"+
                "${if (men<10) "0" else ""}${men}:"+
                "${if (sec<10) "0" else ""}${sec}:"+
                "${if (ms<10) "0" else ""}${ms}"
    }


    fun calculteAllPolyLinesLength(polyline:MutableList<LatLng>):Float
    {
        var destince=0F
        for (i in 0.. polyline.size-2)
        {
            val first=polyline[i]
            val end=polyline[i+1]

            val result=FloatArray(1)
            Location.distanceBetween(
                    first.latitude,
                    first.longitude,
                    end.latitude,
                    end.longitude,
                    result
            )
            destince+=result[0]
        }
        return destince
    }

}