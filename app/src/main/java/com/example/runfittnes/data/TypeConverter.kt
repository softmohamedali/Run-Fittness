package com.example.runfittnes.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class TypeConverter {

    @TypeConverter
    fun toBitmap(byteArray: ByteArray):Bitmap{

        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

    @TypeConverter
    fun toByteArray(bitmap: Bitmap):ByteArray
    {
        val outputStream=ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }
}