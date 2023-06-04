package com.bangkit.allergysense.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.allergysense.R
import com.bangkit.allergysense.databinding.ActivityDetailAllergyBinding

class DetailAllergyActivity : AppCompatActivity() {
    private var _binding: ActivityDetailAllergyBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}