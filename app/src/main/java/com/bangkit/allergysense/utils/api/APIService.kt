package com.bangkit.allergysense.utils.api

import com.bangkit.allergysense.utils.responses.AllergyCheckResponse
import com.bangkit.allergysense.utils.responses.GetDetailHistoryResponse
import com.bangkit.allergysense.utils.responses.GetHistoriesResponse
import com.bangkit.allergysense.utils.responses.GetProfileResponse
import com.bangkit.allergysense.utils.responses.LoginResponse
import com.bangkit.allergysense.utils.responses.QuotesResponse
import com.bangkit.allergysense.utils.responses.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @GET("histories")
    suspend fun getHistories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : GetHistoriesResponse

    @GET("histories/{id}")
    suspend fun getDetailHistory(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ) : GetDetailHistoryResponse

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") auth: String,
    ) : GetProfileResponse

    @GET("quotes")
    suspend fun quotes(
        @Header("Authorization") auth: String,
    ) : QuotesResponse

    @Multipart
    @POST("ml/allergy-check")
    suspend fun check(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("problem") problem: RequestBody,
        @Part("allergen_code_number") code: RequestBody,
    ) : AllergyCheckResponse
}