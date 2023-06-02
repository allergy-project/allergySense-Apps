package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AllergyRepository

class DetailHistoryViewModel(private val allergyRepository: AllergyRepository): ViewModel() {
    fun getDetail(auth: String, id: String) = allergyRepository.getDetailHistory(auth, id)
}