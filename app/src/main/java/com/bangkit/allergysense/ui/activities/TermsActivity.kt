package com.bangkit.allergysense.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager

import com.bangkit.allergysense.R
import com.bangkit.allergysense.databinding.ActivityTermsBinding


class TermsActivity : AppCompatActivity() {
    private var _binding : ActivityTermsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading(false)
        view()
        val id = intent.getStringExtra(EXTRA_ID)
        Log.v("id", id.toString())

        binding.lastUpdate.text = getString(R.string.last_update)
        binding.termsIsian.text = getString(R.string.setuju_terms)
        binding.termsIsian2.text = getString(R.string.terms_isian)

        binding.backarrow.setOnClickListener {
            val intent = Intent(this@TermsActivity, MainActivity::class.java)
            startActivity(intent)

        }
//            val intent = Intent(this@TermsActivity, MainActivity::class.java)
//                .putExtra("checkTerms", "navAllergy")
//            startActivity(intent)
//            finish()
//        }
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
    companion object {
        const val EXTRA_ID = "extra_id"
    }
}