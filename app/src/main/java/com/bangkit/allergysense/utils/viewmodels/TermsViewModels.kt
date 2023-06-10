package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.ViewModel
import com.bangkit.allergysense.utils.repositories.AllergyRepository

class TermsViewModel (val allergyRepository: AllergyRepository): ViewModel() {
    fun getTerms(auth: String) = allergyRepository.getProfile(auth)

}