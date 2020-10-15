package org.csrabolivia.covidapp

class Constants {
    companion object{
        const val NOMBRES: String = "nombres"
        const val APELLIDOS: String = "apellidos"
        const val GENERO: String = "genero"
        const val FNACIMIENTO = "fnacimineto"
        const val TELEFONO = "telefono"
        const val ESTCIVIL = "estCivil"
        const val MUNICIPIO = "municipio"
        const val CIUDAD = "ciudad"
        var primeraVez = false
        var LATITUD = "sin dato"
        var LONGITUD = "sin dato"
        var ALTITUD = "sin dato"
        var PRECISION = "sin dato"
        var TIEMPO = "sin dato"
        var IDUNICO : String = "desconocido"
        const val LOCATION_REQUEST = 1000
        const val GPS_REQUEST = 1001
        var _conDatosGPS = false
    }
}