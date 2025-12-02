package com.example.tuyademo

import android.app.Application
import android.content.Context
import android.util.Log

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            TuyaHomeSdk.init(this)
            TuyaHomeSdk.setDebugMode(true)
            val (appKey, appSecret) = readAppCredentials(this)
            Log.i(TAG, "TuyaHomeSdk initialized. Stored appKey length=${appKey.length}, appSecret length=${appSecret.length}")
        } catch (e: Throwable) {
            Log.e(TAG, "SDK init failed", e)
        }
    }

    companion object {
        private const val TAG = "TuyaDemoApp"
        private const val PREFS = "tuya_demo_prefs"
        private const val KEY_APP_KEY = "app_key"
        private const val KEY_APP_SECRET = "app_secret"

        fun saveAppCredentials(context: Context, appKey: String, appSecret: String) {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putString(KEY_APP_KEY, appKey)
                .putString(KEY_APP_SECRET, appSecret)
                .apply()
        }

        fun readAppCredentials(context: Context): Pair<String, String> {
            val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            val appKey = prefs.getString(KEY_APP_KEY, "") ?: ""
            val appSecret = prefs.getString(KEY_APP_SECRET, "") ?: ""
            return appKey to appSecret
        }
    }
}
