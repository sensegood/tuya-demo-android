package com.example.tuyademo

import android.app.Application
import com.tuya.smart.sdk.TuyaSdk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TuyaSdk.init(this)
        TuyaSdk.setDebugMode(true)
    }
}
