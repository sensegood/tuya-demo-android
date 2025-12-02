package com.example.tuyademo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tuyademo.databinding.ActivityPairingBinding
import com.thingclips.smart.home.sdk.builder.ActivatorBuilder
import com.thingclips.smart.sdk.api.ITuyaActivator
import com.thingclips.smart.sdk.api.ITuyaActivatorGetToken
import com.thingclips.smart.sdk.api.ITuyaSmartActivatorListener
import com.thingclips.smart.sdk.enums.ActivatorModelEnum

class PairingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPairingBinding
    private var activator: ITuyaActivator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPairingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startEzButton.setOnClickListener { requestTokenAndStart() }
        startBleScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        activator?.stop()
        activator?.onDestroy()
    }

    private fun requestTokenAndStart() {
        val ssid = binding.ssidInput.text.toString()
        val password = binding.passwordInput.text.toString()
        binding.pairingStatus.text = "Requesting token..."

        TuyaHomeSdk.getActivatorInstance().getActivatorToken(
            0,
            object : ITuyaActivatorGetToken {
                override fun onSuccess(token: String?) {
                    binding.pairingStatus.text = "Token acquired"
                    if (!token.isNullOrEmpty()) {
                        startEzActivator(token, ssid, password)
                    }
                }

                override fun onFailure(errorCode: String?, errorMsg: String?) {
                    binding.pairingStatus.text = "Token failed: $errorCode $errorMsg"
                }
            }
        )
    }

    private fun startEzActivator(token: String, ssid: String, password: String) {
        activator?.stop()
        activator = TuyaHomeSdk.getActivatorInstance().newActivator(
            ActivatorBuilder()
                .setContext(this)
                .setSsid(ssid)
                .setPassword(password)
                .setActivatorModel(ActivatorModelEnum.TY_EZ)
                .setToken(token)
                .setTimeOut(120)
                .setListener(object : ITuyaSmartActivatorListener {
                    override fun onError(errorCode: String?, errorMsg: String?) {
                        binding.pairingStatus.text = "Error: $errorCode $errorMsg"
                    }

                    override fun onActiveSuccess(deviceBean: com.thingclips.smart.sdk.bean.DeviceBean?) {
                        binding.pairingStatus.text = "Success: ${deviceBean?.devId ?: ""}"
                    }

                    override fun onStep(step: String?, data: Any?) {
                        binding.pairingStatus.text = "Step: $step"
                    }
                })
        )
        activator?.start()
    }

    private fun startBleScan() {
        try {
            val bleManager = TuyaHomeSdk.getBleManager()
            val method = bleManager.javaClass.methods.firstOrNull { it.name == "startLeScan" && it.parameterTypes.isEmpty() }
            if (method != null) {
                method.invoke(bleManager)
                Toast.makeText(this, "BLE scan started", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("PairingActivity", "startLeScan with no args not found")
            }
        } catch (e: Exception) {
            Log.e("PairingActivity", "BLE scan failed", e)
        }
    }
}
