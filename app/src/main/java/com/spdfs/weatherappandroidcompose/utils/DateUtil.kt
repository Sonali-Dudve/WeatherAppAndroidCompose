package com.spdfs.weatherappandroidcompose.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val time = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")

        return time.format(formatter).toUpperCase(Locale.ENGLISH)
    }
}