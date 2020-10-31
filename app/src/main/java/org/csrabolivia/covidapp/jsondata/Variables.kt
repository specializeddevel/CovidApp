package org.csrabolivia.covidapp.jsondata

class Variables {
    companion object{
        var NOMBRES: String = "nombres"
        var APELLIDOS: String = "apellidos"
        var GENERO: String = "genero"
        var FNACIMIENTO = "fnacimiento"
        var TELEFONO = "telefono"
        var ESTCIVIL = "estCivil"
        var MUNICIPIO = "municipio"
        var CIUDAD = "ciudad"
        var BARRIO = "barrio"
        var DIRECCION = "direccion"
        var primeraVez = false
        var LATITUD = "sin dato"
        var LONGITUD = "sin dato"
        var ALTITUD = "sin dato"
        var PRECISION = "sin dato"
        var TIEMPO = "sin dato"
        var IDUNICO : String = "desconocido"
        var EDAD_ANOS: Int? = null
        //antecedentes
        var varAntecedente1: Int? = null
        var varAntecedente2: Int? = null
        var varAntecedente3: Int? = null
        var varAntecedente4: Int? = null
        var varAntecedente5: Int? = null
        var varAntecedente6: Int? = null
        var varAntecedente7: Int? = null
        var varAntecedente8: Int? = null
        var varEmbarazada: Int? = null
        var femenino = false
        const val LOCATION_REQUEST = 1000
        const val GPS_REQUEST = 1001
        var _conDatosGPS = false
        var numeroTelefonoMedico = "tel:+59178177431"
    }
}