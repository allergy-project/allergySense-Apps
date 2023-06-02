package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class AllergyCheckResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: Check? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Check(

	@field:SerializedName("history_id")
	val historyId: String? = null,

	@field:SerializedName("is_allergy")
	val isAllergy: Boolean? = null
)
