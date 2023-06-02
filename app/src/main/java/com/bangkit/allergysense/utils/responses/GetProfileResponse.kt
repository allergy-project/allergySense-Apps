package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: Profile? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profile(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

)
