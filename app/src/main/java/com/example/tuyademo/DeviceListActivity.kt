package com.example.tuyademo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tuyademo.databinding.ActivityDeviceListBinding
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.sdk.bean.DeviceBean

class DeviceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceListBinding
    private var homeId: Long = 0L
    private val adapter = DeviceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeId = intent.getLongExtra(EXTRA_HOME_ID, 0L)

        binding.deviceRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deviceRecyclerView.adapter = adapter

        binding.refreshButton.setOnClickListener { fetchDevices() }
        binding.openControlButton.setOnClickListener {
            val deviceId = binding.deviceIdInput.text.toString()
            if (deviceId.isBlank()) {
                Toast.makeText(this, "Enter a Device ID", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, DeviceControlActivity::class.java).apply {
                    putExtra(DeviceControlActivity.EXTRA_DEVICE_ID, deviceId)
                })
            }
        }

        fetchDevices()
    }

    private fun fetchDevices() {
        if (homeId <= 0L) {
            Toast.makeText(this, "Missing home id", Toast.LENGTH_LONG).show()
            return
        }

        TuyaHomeSdk.getHomeInstance(homeId).getHomeDetail(object : ITuyaHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                val devices = bean?.deviceList ?: emptyList()
                adapter.submitList(devices)
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                val message = "Error: ${'$'}errorCode - ${'$'}errorMsg"
                Toast.makeText(this@DeviceListActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        const val EXTRA_HOME_ID = "home_id"
    }

    private class DeviceAdapter : RecyclerView.Adapter<DeviceViewHolder>() {

        private val items = mutableListOf<DeviceBean>()

        fun submitList(devices: List<DeviceBean>) {
            items.clear()
            items.addAll(devices)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            return DeviceViewHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    private class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(android.R.id.text1)
        private val subtitle: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(deviceBean: DeviceBean) {
            title.text = deviceBean.name
            subtitle.text = deviceBean.devId
        }
    }
}
