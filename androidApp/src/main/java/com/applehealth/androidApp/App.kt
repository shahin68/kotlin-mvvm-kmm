package com.applehealth.androidApp

import android.app.Application
import com.applehealth.androidApp.di.mainModule
import com.applehealth.androidApp.di.puzzlesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        koin()
    }

    private fun koin() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    mainModule,
                    puzzlesModule
                )
            )
        }
    }
}

