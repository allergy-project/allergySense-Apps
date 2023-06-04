package com.bangkit.allergysense.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.allergysense.R
import com.bangkit.allergysense.databinding.ActivityVerifyBinding

class VerifyActivity : AppCompatActivity() {
    private var _binding: ActivityVerifyBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOk.setOnClickListener {
            val intent = Intent(this@VerifyActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}