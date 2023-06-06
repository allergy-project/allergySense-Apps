package com.bangkit.allergysense.utils.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.utils.api.APIInjection
import com.bangkit.allergysense.utils.repositories.AuthRepository

class AuthViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthViewModelFactory = instance ?: synchronized(this) {
            instance ?: AuthViewModelFactory(APIInjection.provideAuthRepository(dataStore))
        }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(authRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(authRepository) as T
            modelClass.isAssignableFrom(LogoutViewModel::class.java) -> LogoutViewModel(authRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }

    }
}