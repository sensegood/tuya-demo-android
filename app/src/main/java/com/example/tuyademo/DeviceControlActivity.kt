package com.example.tuyademo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tuyademo.databinding.ActivityDeviceControlBinding
import org.json.JSONObject

class DeviceControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceControlBinding
    private var deviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deviceId = intent.getStringExtra(EXTRA_DEVICE_ID)
        binding.deviceIdText.text = deviceId ?: "Unknown device"

        binding.onButton.setOnClickListener { publishSwitchDp(true) }
        binding.offButton.setOnClickListener { publishSwitchDp(false) }
    }

    private fun publishSwitchDp(isOn: Boolean) {
        val devId = deviceId ?: return
        val device = TuyaHomeSdk.newDeviceInstance(devId)
        val dps = JSONObject().apply { put("1", isOn) }
        device.publishDps(dps.toString(), object : com.thingclips.smart.sdk.api.IResultCallback {
            override fun onError(code: String?, error: String?) {
                Toast.makeText(this@DeviceControlActivity, "DP error: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                Toast.makeText(this@DeviceControlActivity, "Sent: $isOn", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val EXTRA_DEVICE_ID = "extra_device_id"
    }
}
