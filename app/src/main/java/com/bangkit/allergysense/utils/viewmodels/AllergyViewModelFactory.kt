package com.bangkit.allergysense.utils.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.utils.api.APIInjection
import com.bangkit.allergysense.utils.repositories.AllergyRepository

class AllergyViewModelFactory(private val allergyRepository: AllergyRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuotesViewModel::class.java)) {
            return QuotesViewModel(allergyRepository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(allergyRepository) as T
        } else if (modelClass.isAssignableFrom(CheckViewModel::class.java)) {
            return CheckViewModel(allergyRepository) as T
        } else if (modelClass.isAssignableFrom(HistoriesViewModel::class.java)) {
            return HistoriesViewModel(allergyRepository) as T
        } else if (modelClass.isAssignableFrom(DetailHistoryViewModel::class.java)) {
            return DetailHistoryViewModel(allergyRepository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel")
    }

    companion object {
        @Volatile
        private var instance: AllergyViewModelFactory? = null
        fun getIntance(dataStore: DataStore<Preferences>): AllergyViewModelFactory = instance ?: synchronized(this) {
            instance ?: AllergyViewModelFactory(APIInjection.provideAllergyRepository(dataStore))
        }.also { instance = it }
    }
}