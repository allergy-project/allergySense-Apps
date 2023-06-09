package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AllergyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CheckViewModel(private val allergyRepository: AllergyRepository): ViewModel() {
    fun check(auth: String, img: MultipartBody.Part, problem: RequestBody, code: RequestBody) = allergyRepository.checkAllergy(auth, img, problem, code)
}