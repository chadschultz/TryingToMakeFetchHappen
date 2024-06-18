package com.example.tryingtomakefetchhappen.log

import android.util.Log
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority <= Log.DEBUG) {
            return
        }

        // In a production environment, code would be here to log to Sentry, Crashlytics, etc.
    }
}