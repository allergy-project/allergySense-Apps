package com.bangkit.allergysense.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.databinding.ActivityRegisterBinding
import com.bangkit.allergysense.utils.repositories.AuthRepository
import com.bangkit.allergysense.utils.repositories.Response
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private var _modelRegister: RegisterViewModel? = null
    private val modelRegister get() = _modelRegister!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading(false)
        view()
        viewPass()
        viewVPass()

        _modelRegister = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[RegisterViewModel::class.java]
        binding.logHere.setOnClickListener { finish() }
        register()
    }

    private fun register() {
        binding.btnReg.setOnClickListener {
            val email = binding.etmail.text.toString()
            val username = binding.etusername.text.toString()
            val pass = binding.etPass.text.toString()
            val vPass = binding.etVPass.text.toString()

            when {
                email.isEmpty() -> binding.etmail.error = "Your email is empty! Please fill it!"
                !email.isValidEmail() -> binding.etmail.error = "Your email is not valid!"
                username.isEmpty() -> binding.etusername.error = "Your username is empty! Please fill it!"
                pass.isEmpty() -> binding.etPass.error = "Your password is empty! Please fill it!"
                pass.length < 8 -> binding.etPass.error = "Password minimum has 8 characters"
                vPass != pass -> binding.etVPass.error = "Password is not same! Please check it again!"
                else -> {
                    modelRegister.reg(username, email, pass).observe(this@RegisterActivity) { result->
                        if (result != null) {
                            when (result) {
                                is Response.Loading -> loading(true)
                                is Response.Success ->  {
                                   loading(false)
                                   Toast.makeText(this@RegisterActivity, "Account registered successfully", Toast.LENGTH_SHORT).show()
                                   CoroutineScope(Dispatchers.IO).launch {
                                       delay(1000)
                                       val intent = Intent(this@RegisterActivity, VerifyActivity::class.java)
                                       intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                       startActivity(intent)
                                   }
                               }
                                is Response.Error -> {
                                    loading(false)
                                    Toast.makeText(this@RegisterActivity, result.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun viewPass() {
        binding.visible.setOnClickListener {
            binding.invisible.visibility = View.VISIBLE
            binding.visible.visibility = View.INVISIBLE
            binding.etPass.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.invisible.setOnClickListener {
            binding.invisible.visibility = View.INVISIBLE
            binding.visible.visibility = View.VISIBLE
            binding.etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    private fun viewVPass() {
        binding.visible.setOnClickListener {
            binding.invisible.visibility = View.VISIBLE
            binding.visible.visibility = View.INVISIBLE
            binding.etVPass.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.invisible.setOnClickListener {
            binding.invisible.visibility = View.INVISIBLE
            binding.visible.visibility = View.VISIBLE
            binding.etVPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    private fun CharSequence.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

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