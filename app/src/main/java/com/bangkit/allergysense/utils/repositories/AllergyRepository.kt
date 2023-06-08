package com.bangkit.allergysense.utils.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.allergysense.utils.api.APIService
import com.bangkit.allergysense.utils.responses.AllergyCheckResponse
import com.bangkit.allergysense.utils.responses.Data
import com.bangkit.allergysense.utils.responses.DataItem
import com.bangkit.allergysense.utils.responses.Profile
import com.bangkit.allergysense.utils.responses.Quote
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class AllergyRepository(private val apiService: APIService) {
    fun getHistories(token: String) : LiveData<Response<List<DataItem>>> = liveData{
        emit(Response.Loading)
        try {
            val service = apiService.getHistories(token)
            val response = service.data
            val data = response.map {
                DataItem(
                    it.allergy,
                    it.createdAt,
                    it.id
                )
            }
            emit(Response.Success(data))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string()?.let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }

    fun getDetailHistory(auth: String, id: String): LiveData<Response<Data>> = liveData {
        emit(Response.Loading)
        try {
            val service = apiService.getDetailHistory(auth, id)
            val response = service.data
            val data = Data(
                response?.id,
                response?.imageUrl,
                response?.allergy,
                response?.createdAt,
                response?.problem,
                response?.suggest
            )
            emit(Response.Success(data))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string()?.let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }

    fun getProfile(auth: String): LiveData<Response<Profile>> = liveData {
        emit(Response.Loading)
        try {
            val service = apiService.getProfile(auth)
            val response = service.data
            val profile = Profile(
                response?.imageUrl,
                response?.username,
                response?.email,
            )
            emit(Response.Success(profile))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string()?.let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }

    fun getQuote(auth: String): LiveData<Response<Quote>> = liveData {
        emit(Response.Loading)
        try {
            val service = apiService.quotes(auth)
            val response = service.data
            val quote = Quote(
                response?.quote,
                response?.author,
            )
            emit(Response.Success(quote))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string()?.let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }

    fun checkAllergy(auth: String, img: MultipartBody.Part, problem: RequestBody, code: RequestBody): LiveData<Response<AllergyCheckResponse>> = liveData {
        emit(Response.Loading)
        try {
            val service = apiService.check(auth, img, problem, code)
            emit(Response.Success(service))
        } catch (e: HttpException) {
            emit(Response.Error(
                try {
                    e.response()?.errorBody()?.string()?.let { JSONObject(it).get("message") }
                } catch (e: JSONException) {
                    e.localizedMessage
                } as String
            ))
        }
    }
}