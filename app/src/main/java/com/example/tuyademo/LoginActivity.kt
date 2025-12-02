package com.example.tuyademo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tuyademo.databinding.ActivityLoginBinding
import com.thingclips.smart.android.user.api.ILoginCallback

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* handled silently */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        populateStoredCredentials()
        requestNeededPermissions()

        binding.loginButton.setOnClickListener { performLogin() }
        binding.pairingButton.setOnClickListener {
            startActivity(Intent(this, PairingActivity::class.java))
        }
        binding.deviceListButton.setOnClickListener {
            startActivity(Intent(this, DeviceListActivity::class.java))
        }
    }

    private fun populateStoredCredentials() {
        val (appKey, appSecret) = App.readAppCredentials(this)
        binding.appKeyInput.setText(appKey)
        binding.appSecretInput.setText(appSecret)
    }

    private fun performLogin() {
        val appKey = binding.appKeyInput.text.toString()
        val appSecret = binding.appSecretInput.text.toString()
        val countryCode = binding.countryCodeInput.text.toString().ifBlank { "1" }
        val account = binding.accountInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (account.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Enter account and password", Toast.LENGTH_SHORT).show()
            return
        }
        App.saveAppCredentials(this, appKey, appSecret)

        TuyaHomeSdk.getUserInstance().loginWithEmail(
            countryCode,
            account,
            password,
            object : ILoginCallback {
                override fun onSuccess(user: com.thingclips.smart.android.user.bean.User?) {
                    Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                }

                override fun onError(code: String?, error: String?) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed: $code $error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun requestNeededPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions += listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        }
        if (permissions.any {
                ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }
}
