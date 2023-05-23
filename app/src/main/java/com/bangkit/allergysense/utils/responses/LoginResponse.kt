package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
