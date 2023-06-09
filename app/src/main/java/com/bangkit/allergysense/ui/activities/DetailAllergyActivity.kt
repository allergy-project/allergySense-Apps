package com.bangkit.allergysense.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.databinding.ActivityDetailAllergyBinding
import com.bangkit.allergysense.utils.repositories.Response
import com.bangkit.allergysense.utils.responses.Data
import com.bangkit.allergysense.utils.viewmodels.AllergyViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.DetailHistoryViewModel
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class DetailAllergyActivity : AppCompatActivity() {
    private var _binding: ActivityDetailAllergyBinding? = null
    private val binding get() = _binding!!
    private var _modelUser: LoginViewModel? = null
    private val modelUser get() = _modelUser!!
    private var _modelDetail: DetailHistoryViewModel? = null
    private val modelDetail get() = _modelDetail!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading(false)
        view()

        _modelUser = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LoginViewModel::class.java]
        _modelDetail = ViewModelProvider(this, AllergyViewModelFactory.getIntance())[DetailHistoryViewModel::class.java]

        val id = intent.getStringExtra(EXTRA_ID)
        Log.v("id", id.toString())

        binding.backarrow.setOnClickListener {
            if (intent.getStringExtra("result")?.equals("detail") == true) {
                val intent = Intent(this@DetailAllergyActivity, UploadAllergyActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@DetailAllergyActivity, MainActivity::class.java)
                    .putExtra("detailCheck", "navAllergy")
                startActivity(intent)
                finish()
            }
        }

        modelUser.user().observe(this@DetailAllergyActivity) {
            id?.let { it1 ->
                modelDetail.getDetail(it.token, it1).observe(this@DetailAllergyActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Response.Loading -> loading(true)
                            is Response.Success -> {
                                loading(false)
                                val allergy = result.data
                                detail(allergy)
                                Toast.makeText(this@DetailAllergyActivity, "Successfully loaded allergy details", Toast.LENGTH_SHORT).show()
                            }

                            is Response.Error -> {
                                loading(false)
                                Toast.makeText(this@DetailAllergyActivity, "Failed to load allergy details", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun detail (allergy: Data) {
        val created = allergy.createdAt ?: 0
        val date = Date(created)
        val format = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale.getDefault())
        val formatted = format.format(date)
        Glide.with(this)
            .load(allergy.imageUrl)
            .into(binding.ivAllergy)
        binding.allergy.text = allergy.allergy
        binding.date.text = formatted
        binding.whyDesc.text = allergy.problem
        binding.howDesc.text = allergy.suggest
        Log.v("Allergy", allergy.allergy.toString())
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

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}