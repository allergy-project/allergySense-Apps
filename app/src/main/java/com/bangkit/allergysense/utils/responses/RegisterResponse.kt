package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
