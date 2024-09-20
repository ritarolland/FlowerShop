package com.example.prac1.app

import android.app.Application
import com.example.prac1.di.AppComponent
import com.example.prac1.di.DaggerAppComponent

class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}