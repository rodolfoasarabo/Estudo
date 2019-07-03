package com.rodolfo.movies.base

import android.app.Application
import com.instabug.library.Instabug
import com.instabug.library.invocation.InstabugInvocationEvent
import com.rodolfo.movies.module.moviesModule
import org.koin.android.ext.android.startKoin

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(moviesModule))
        initializeInstaBug()
    }

    private fun initializeInstaBug(){
        Instabug.Builder(this, "57cd076edc2af7c8cf378c742abceefb")
                .setInvocationEvents(InstabugInvocationEvent.SCREENSHOT, InstabugInvocationEvent.SHAKE)
                .build()
    }
}