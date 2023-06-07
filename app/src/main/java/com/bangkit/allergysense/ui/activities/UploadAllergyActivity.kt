package com.bangkit.allergysense.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.R
import com.bangkit.allergysense.databinding.ActivityUploadAllergyBinding
import com.bangkit.allergysense.ui.fragments.allergy.AllergyFragment
import com.bangkit.allergysense.utils.helpers.uriToFile
import com.bangkit.allergysense.utils.repositories.Response
import com.bangkit.allergysense.utils.viewmodels.AllergyViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.CheckViewModel
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class UploadAllergyActivity : AppCompatActivity() {
    private var _binding: ActivityUploadAllergyBinding? = null
    private val binding get() = _binding!!
    private var _modelUser: LoginViewModel? = null
    private val modelUser get() = _modelUser!!
    private var _modelUpload: CheckViewModel? = null
    private val modelUpload get() = _modelUpload!!
    private var myFile: File? = null
    private var selectedFood: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUploadAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading(false)
        view()
        spinner()

        _modelUser = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LoginViewModel::class.java]
        _modelUpload = ViewModelProvider(this, AllergyViewModelFactory.getIntance())[CheckViewModel::class.java]

        binding.btnGal.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose Image")
            launcherIntentGallery.launch(intent)
        }

        binding.backarrow.setOnClickListener {
            val intent = Intent(this@UploadAllergyActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCek.setOnClickListener {
            when {
                myFile == null -> Toast.makeText(this@UploadAllergyActivity, "Choose Image", Toast.LENGTH_SHORT).show()
                binding.etHow.text.length > 150 -> binding.etHow.error = "Maximal word is 150"
                binding.etHow.text.isEmpty() -> binding.etHow.error = "Description is empty! Please fill it!"
                binding.foodSpinner.selectedItemPosition == -1 -> Toast.makeText(this@UploadAllergyActivity, "Please choose one ingredient", Toast.LENGTH_SHORT).show()
                else -> {
                    val file = compress(myFile as File)
                    val etHow = binding.etHow.text.toString().toRequestBody("text/plain".toMediaType())
                    val img = file.asRequestBody("image/jpeg".toMediaType())
                    val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("allergy_image", file.name, img)
                    val foodArray = selectedFood?.toString()?.toRequestBody("text/plain".toMediaType())
                    modelUser.user().observe(this@UploadAllergyActivity) { user ->
                        if (user.token.isNotEmpty()) {
                            if (foodArray != null) {
                                modelUpload.check(user.token, imgMultipart, etHow, foodArray).observe(this@UploadAllergyActivity) {result ->
                                    if (result != null) {
                                        when (result) {
                                            is Response.Loading -> loading(true)
                                            is Response.Success -> {
                                                loading(false)
                                                Toast.makeText(this@UploadAllergyActivity, "Check for allergies", Toast.LENGTH_SHORT).show()
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    delay(1000)
                                                    val intent = Intent(this@UploadAllergyActivity, DetailAllergyActivity::class.java)
                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(intent)
                                                }
                                            }
                                            is Response.Error -> {
                                                loading(false)
                                                Toast.makeText(this@UploadAllergyActivity, result.message, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun spinner() {
        val spinner: Spinner = binding.foodSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.food,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedFood = pos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //no item
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val file = uriToFile(selectedImg, this@UploadAllergyActivity)
            myFile = file
            binding.ivAllergy.setImageURI(selectedImg)
        }
    }

    private fun compress(file: File) : File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var quality = 100
        var length: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            length = bmpPicByteArray.size
            quality -= 6
        } while (length > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, FileOutputStream(file))
        return file
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