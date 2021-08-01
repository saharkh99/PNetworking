package com.example.pnetworking
import android.app.Application
import com.example.pnetworking.di.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(signupmodule, testmodule,profilemodule,connectionmodule, followmodule,
                pchatmodule,chatFmodule,settingsmodule))
        }
    }
}