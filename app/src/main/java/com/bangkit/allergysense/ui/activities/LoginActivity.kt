package com.bangkit.allergysense.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.databinding.ActivityLoginBinding
import com.bangkit.allergysense.utils.repositories.Response
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var _modelLogin: LoginViewModel? = null
    private val modelLogin get() = _modelLogin!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loading(false)
        view()

        _modelLogin = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LoginViewModel::class.java]
        binding.btnlog.setOnClickListener {
            val username = binding.etusername.text.toString()
            val pass = binding.etPass.text.toString()
            modelLogin.login(username, pass).observe(this@LoginActivity) {result ->
                if (result != null) {
                    when (result) {
                        is Response.Loading -> loading(true)
                        is Response.Success -> {
                            loading(false)
                            Toast.makeText(this@LoginActivity, "Welcome to Allergy Sense", Toast.LENGTH_SHORT).show()
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(2000)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        }
                        is Response.Error -> {
                            loading(false)
                            Toast.makeText(this@LoginActivity, "Your username or password is wrong!!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun view() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.insetsController?.hide(
            WindowInsets.Type.statusBars()) else window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    private fun loading(isLoad: Boolean) {
        if (isLoad) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}