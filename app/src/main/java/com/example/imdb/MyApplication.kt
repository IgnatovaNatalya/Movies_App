package com.example.imdb

import android.app.Application
import com.example.imdb.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(koinModule)
        }
    }
}