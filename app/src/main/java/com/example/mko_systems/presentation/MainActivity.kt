package com.example.mko_systems.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mko_systems.R
import com.example.mko_systems.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            if (!isAccessibilityServiceEnabled()) {
                openAccessibilityServiceSettings()
            } else {
                launchInstagram()
            }
        }

        viewModel.accountName.observe(this) { name ->
            binding.textView.text = name
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAccountName()
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
        val packageName = packageName
        return enabledServices?.contains(packageName) == true
    }

    private fun openAccessibilityServiceSettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    private fun launchInstagram() {
        val uri = Uri.parse(getString(R.string.instagram_profile_uri))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(getString(R.string.com_instagram_android))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.instagram_is_not_installed),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}