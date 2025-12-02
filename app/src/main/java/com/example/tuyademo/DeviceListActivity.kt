package com.example.tuyademo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tuyademo.databinding.ActivityDeviceListBinding
import com.thingclips.smart.sdk.bean.DeviceBean

class DeviceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deviceRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deviceRecyclerView.adapter = DeviceAdapter(TuyaHomeSdk.getDataInstance().deviceBeanList)
    }

    inner class DeviceAdapter(private val devices: List<DeviceBean>) :
        RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

        inner class DeviceViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
            return DeviceViewHolder(view)
        }

        override fun getItemCount(): Int = devices.size

        override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
            val device = devices[position]
            holder.textView.text = device.name ?: device.devId
            holder.textView.setOnClickListener {
                val intent = Intent(this@DeviceListActivity, DeviceControlActivity::class.java)
                intent.putExtra(DeviceControlActivity.EXTRA_DEVICE_ID, device.devId)
                startActivity(intent)
            }
        }
    }
}
