package com.bangkit.allergysense.utils.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.utils.api.APIInjection
import com.bangkit.allergysense.utils.repositories.AuthRepository

class AuthViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile private var instance: AuthViewModelFactory? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthViewModelFactory = instance ?: synchronized(this) {
            instance ?: AuthViewModelFactory(APIInjection.provideAuthRepository(dataStore))
        }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(LogoutViewModel::class.java)) {
            return LogoutViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}