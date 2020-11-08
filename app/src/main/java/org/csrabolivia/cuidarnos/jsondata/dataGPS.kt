package org.csrabolivia.cuidarnos.jsondata

import com.google.gson.annotations.SerializedName

data class DataGPS(

	@field:SerializedName("latitud")
	val latitud: Double? = null,

	@field:SerializedName("longitud")
	val longitud: Double? = null,

	@field:SerializedName("tiempo")
	val tiempo: Long? = null,

	@field:SerializedName("precision")
	val precision: Double? = null,

	@field:SerializedName("altitud")
	val altitud: Double? = null
)
