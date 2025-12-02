package com.example.tuyademo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tuyademo.databinding.ActivityPairingBinding
import com.tuya.smart.android.common.utils.L
import com.tuya.smart.android.device.api.ITuyaActivator
import com.tuya.smart.android.device.api.ITuyaSmartActivatorListener
import com.tuya.smart.android.mvp.bean.Result
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.bean.MultiModeActivatorBean
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.enums.ActivatorModelEnum

class PairingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPairingBinding
    private var activator: ITuyaActivator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPairingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startPairingButton.setOnClickListener {
            val homeId = intent.getLongExtra(EXTRA_HOME_ID, 0L).takeIf { it > 0 }
                ?: binding.homeIdInput.text.toString().toLongOrNull()
            val ssid = binding.ssidInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (homeId == null || homeId <= 0L) {
                Toast.makeText(this, "Enter a valid Home ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (ssid.isBlank()) {
                Toast.makeText(this, "Enter Wi-Fi SSID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            requestTokenAndStart(homeId, ssid, password)
        }

        binding.stopPairingButton.setOnClickListener {
            activator?.stop()
            Toast.makeText(this, "Pairing stopped", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestTokenAndStart(homeId: Long, ssid: String, password: String) {
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, object : Result.Listener<String> {
            override fun onFailure(p0: String?, p1: String?) {
                val message = "Token error: ${'$'}p0 - ${'$'}p1"
                Toast.makeText(this@PairingActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onSuccess(token: String?) {
                startMultiModeActivator(homeId, ssid, password, token.orEmpty())
            }
        })
    }

    private fun startMultiModeActivator(homeId: Long, ssid: String, password: String, token: String) {
        val listener = object : ITuyaSmartActivatorListener {
            override fun onError(errorCode: String?, errorMsg: String?) {
                val message = "Pairing failed: ${'$'}errorCode - ${'$'}errorMsg"
                Toast.makeText(this@PairingActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onActiveSuccess(deviceBean: DeviceBean?) {
                val message = "Device paired: ${'$'}{deviceBean?.name ?: "Unknown"}"
                Toast.makeText(this@PairingActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onStep(step: String?, data: Any?) {
                L.d("PairingActivity", "step=${'$'}step data=${'$'}data")
            }
        }

        val activatorBean = MultiModeActivatorBean.Builder()
            .setContext(this)
            .setPassword(password)
            .setSsid(ssid)
            .setActivatorModel(ActivatorModelEnum.TY_EZ_MULTI_MODE)
            .setTimeOut(120)
            .setToken(token)
            .setHomeId(homeId)
            .setListener(listener)
            .build()

        activator?.stop()
        activator = TuyaHomeSdk.getActivatorInstance().newMultiModeActivator(activatorBean)
        activator?.start()
        Toast.makeText(this, "Pairing started", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        activator?.stop()
        activator?.onDestroy()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_HOME_ID = "home_id"
    }
}
