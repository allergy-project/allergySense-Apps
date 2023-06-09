package com.bangkit.allergysense.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.allergysense.R
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

        binding.about.text = getString(R.string.about_apps)
        binding.welcometoapps.text = getString(R.string.welcome_about)
        binding.aboutIsian.text = getString(R.string.PengantarAbout)
        binding.aboutIsian2.text = getString(R.string.PenjelasanAbout)
        binding.backarrow.setOnClickListener {
            val intent = Intent(this@AboutActivity, MainActivity::class.java)
            startActivity(intent)

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