package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class GetHistoriesResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("allergy")
	val allergy: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("id")
	val id: String? = null,
)
