package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AllergyRepository

class QuotesViewModel(private val allergyRepository: AllergyRepository): ViewModel() {
    fun quotes(auth: String) = allergyRepository.getQuote(auth)
}