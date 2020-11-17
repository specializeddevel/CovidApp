package org.csrabolivia.cuidarnos.jsondata

import com.google.gson.annotations.SerializedName

data class DataUsuario(

	@field:SerializedName("pin")
	val pin: Int? = null,

	@field:SerializedName("apellidos")
	val apellidos: String? = null,

	@field:SerializedName("barrio")
	val barrio: String? = null,

	@field:SerializedName("idUnico")
	val idUnico: String? = null,

	@field:SerializedName("municipio")
	val municipio: String? = null,

	@field:SerializedName("ciudad")
	val ciudad: String? = null,

	@field:SerializedName("genero")
	val genero: String? = null,

	@field:SerializedName("direccion")
	val direccion: String? = null,

	@field:SerializedName("fnacimiento")
	val fnacimiento: String? = null,

	@field:SerializedName("estCivil")
	val estCivil: String? = null,

	@field:SerializedName("telefono")
	val telefono: String? = null,

	@field:SerializedName("nombres")
	val nombres: String? = null


)
