package com.bangkit.allergysense.utils.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){
    fun user() : Flow<User> {
        return dataStore.data.map {
            User(
                it[TOKEN] ?: "",
                it[ISLOG] ?: false,
            )
        }
    }

    suspend fun log(islog: Boolean, token: String) {
        dataStore.edit {
            it[ISLOG] = islog
            it[TOKEN] = token
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[ISLOG] = false
            it[TOKEN] = ""
        }
    }

    companion object {
        private val ISLOG = booleanPreferencesKey("islog")
        private val TOKEN = stringPreferencesKey("token")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}