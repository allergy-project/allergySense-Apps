package com.bangkit.allergysense.utils.responses

import com.google.gson.annotations.SerializedName

data class GetDetailHistoryResponse(

	@field:SerializedName("status_coe")
	val statusCoe: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("allergy")
	val allergy: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("problem")
	val problem: String? = null,

	@field:SerializedName("suggest")
	val suggest: String? = null,

)
