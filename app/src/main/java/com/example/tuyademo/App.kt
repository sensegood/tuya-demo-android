package com.example.tuyademo

import android.app.Application
import com.thingclips.smart.home.sdk.ThingHomeSdk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ThingHomeSdk.init(this)
        ThingHomeSdk.setDebugMode(true)
    }
}
