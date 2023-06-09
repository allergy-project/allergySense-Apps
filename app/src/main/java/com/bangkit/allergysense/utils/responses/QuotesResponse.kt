package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class QuotesResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: Quote? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Quote(

	@field:SerializedName("quote")
	val quote: String? = null,

	@field:SerializedName("author")
	val author: String? = null,
)
