package com.bangkit.allergysense.utils.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.allergysense.utils.repositories.AllergyRepository
import com.bangkit.allergysense.utils.responses.DataItem

class HistoriesViewModel(private val allergyRepository: AllergyRepository): ViewModel() {
    fun getHistories(token: String) = allergyRepository.getHistories(token)
}