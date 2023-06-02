package com.bangkit.allergysense.utils.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.bangkit.allergysense.utils.api.APIService
import com.bangkit.allergysense.utils.preferences.User
import com.bangkit.allergysense.utils.preferences.UserPreferences
import com.bangkit.allergysense.utils.responses.LoginResponse
import com.bangkit.allergysense.utils.responses.RegisterResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository(private val apiService: APIService, private val userPreferences: UserPreferences) {
    fun user(): LiveData<User> = userPreferences.user().asLiveData()

    fun logout() = CoroutineScope(Dispatchers.IO).launch {
        userPreferences.logout()
    }

    fun log (username: String, pass: String): LiveData<Response<LoginResponse>> = liveData {
        emit(Response.Loading)
        try {
            val service = apiService.login(username, pass)
            service.token?.let { userPreferences.log(true, it) }
            emit(Response.Success(service))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string().let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }

    fun reg(username: String, email: String, pass: String): LiveData<Response<RegisterResponse>> = liveData {
        emit(Response.Loading)
        try {
            val service = apiService.register(username, email, pass)
            emit(Response.Success(service))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string().let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }
}