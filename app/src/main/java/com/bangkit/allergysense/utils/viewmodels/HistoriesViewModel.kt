package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AllergyRepository

class HistoriesViewModel(private val allergyRepository: AllergyRepository): ViewModel() {
    fun getHistories(token: String) = allergyRepository.getHistories(token)
}