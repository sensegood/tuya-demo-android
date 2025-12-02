package com.example.tuyademo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tuyademo.databinding.ActivityLoginBinding
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.bean.User
import com.tuya.smart.home.sdk.TuyaHomeSdk

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            performAnonymousLogin()
        }

        binding.openDeviceListButton.setOnClickListener {
            val homeId = binding.homeIdInput.text.toString().toLongOrNull()
            if (homeId == null) {
                Toast.makeText(this, "Enter a valid Home ID", Toast.LENGTH_SHORT).show()
            } else {
                openDeviceList(homeId)
            }
        }

        binding.openPairingButton.setOnClickListener {
            val homeId = binding.homeIdInput.text.toString().toLongOrNull()
            if (homeId == null) {
                Toast.makeText(this, "Enter a valid Home ID", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, PairingActivity::class.java).apply {
                    putExtra(PairingActivity.EXTRA_HOME_ID, homeId)
                })
            }
        }
    }

    private fun performAnonymousLogin() {
        TuyaHomeSdk.getUserInstance().loginWithAnonymity(object : ILoginCallback {
            override fun onSuccess(user: User?) {
                Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
            }

            override fun onError(code: String?, error: String?) {
                val message = "Login failed: ${'$'}code - ${'$'}error"
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun openDeviceList(homeId: Long) {
        startActivity(Intent(this, DeviceListActivity::class.java).apply {
            putExtra(DeviceListActivity.EXTRA_HOME_ID, homeId)
        })
    }
}
