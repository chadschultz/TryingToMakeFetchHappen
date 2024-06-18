package com.example.tryingtomakefetchhappen

import android.app.Application
import com.example.tryingtomakefetchhappen.log.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class TryingToMakeFetchHappenApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        } else {
            plant(CrashReportingTree())
        }
    }
}