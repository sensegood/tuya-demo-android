package com.example.tuyademo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tuyademo.databinding.ActivityDeviceControlBinding
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IResultCallback
import com.tuya.smart.sdk.api.ITuyaDevice

class DeviceControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceControlBinding
    private var tuyaDevice: ITuyaDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deviceId = intent.getStringExtra(EXTRA_DEVICE_ID).orEmpty()
        tuyaDevice = TuyaHomeSdk.newDeviceInstance(deviceId)

        binding.readDpButton.setOnClickListener { showCurrentDps() }
        binding.writeDpButton.setOnClickListener { publishDp() }
    }

    private fun showCurrentDps() {
        val current = tuyaDevice?.devId?.let { TuyaHomeSdk.getDataInstance().getDps(it) }
        binding.dpStateText.text = current?.toString() ?: "No cache available"
    }

    private fun publishDp() {
        val dpKey = binding.dpKeyInput.text.toString()
        val dpValue = binding.dpValueInput.text.toString()

        if (dpKey.isBlank()) {
            Toast.makeText(this, "DP Key required", Toast.LENGTH_SHORT).show()
            return
        }

        val payload = mapOf(dpKey to dpValue)
        tuyaDevice?.publishDps(payload, object : IResultCallback {
            override fun onSuccess() {
                Toast.makeText(this@DeviceControlActivity, "DP sent", Toast.LENGTH_SHORT).show()
            }

            override fun onError(code: String?, error: String?) {
                val message = "Failed: ${'$'}code - ${'$'}error"
                Toast.makeText(this@DeviceControlActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        tuyaDevice?.onDestroy()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_DEVICE_ID = "device_id"
    }
}
