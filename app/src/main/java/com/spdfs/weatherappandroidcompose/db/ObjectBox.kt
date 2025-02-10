package com.spdfs.weatherappandroidcompose.db

import android.content.Context
import io.objectbox.BoxStore

object ObjectBox {
    lateinit var boxStore: BoxStore

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}