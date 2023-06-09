package com.bangkit.allergysense.utils.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.bangkit.allergysense.utils.preferences.UserPreferences
import com.bangkit.allergysense.utils.repositories.AllergyRepository
import com.bangkit.allergysense.utils.repositories.AuthRepository

object APIInjection {
    fun provideAuthRepository(dataStore: DataStore<Preferences>) : AuthRepository {
        val apiService = APIConfig.getApiService()
        val userPreferences = UserPreferences.getInstance(dataStore)
        return AuthRepository(apiService, userPreferences)
    }
    fun provideAllergyRepository() : AllergyRepository {
        val apiService = APIConfig.getApiService()
        return AllergyRepository(apiService)
    }
}