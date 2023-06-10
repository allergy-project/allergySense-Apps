package com.bangkit.allergysense.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.allergysense.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private var _binding: ActivityAboutBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading(false)
        view()

        binding.backarrow.setOnClickListener {
            finish()
        }
    }
    private fun loading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun view() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.insetsController?.hide(
            WindowInsets.Type.statusBars()) else window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}