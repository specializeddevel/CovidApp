package org.csrabolivia.covidapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import org.csrabolivia.covidapp.jsondata.DataAntecedentes
import org.csrabolivia.covidapp.jsondata.DataGPS
import org.csrabolivia.covidapp.jsondata.DataUsuario
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private val key = "PERSONALDATA"
    private val keyGps = "GPSDATA"
    private val keyID = "IDUNICO"
    private val keyAntecedentes = "ANTECEDENTESDATA"
    private var isGPS = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (verificaInternet()){
            // notification()
            //btContinuar.setOnClickListener(this::escucharBtn)
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            /*val editor = prefs.edit()
            editor.remove("PERSONALDATA")
            editor.remove("IDUNICO")
            editor.remove("GPSDATA")
            editor.apply()*/
            val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            val bundle = Bundle()
            bundle.putString("InitScreen", "Integración con Firebase completada")
            analytics.logEvent("InitScreen", bundle)
            //verifica datos de ID unica
            val tieneIDUnica = prefs.getString(keyID, "SD")
            if(!tieneIDUnica.equals("SD")){
                Variables.IDUNICO = tieneIDUnica.toString()
                Log.i("Cuidarnos", "Se tienen ID unica registrada: ${Variables.IDUNICO}")
            } else {
                Log.i("Cuidarnos", "No se tienen ID unica generada, se intentara generarla")
                generarIDUnico()
            }
            val conDatosGPS = prefs.getString(keyGps, "SD")
            if(!conDatosGPS.equals("SD")){
                Variables._conDatosGPS = true
                cargaDatosGPS(conDatosGPS.toString())
                Log.i("Cuidarnos", "Se tienen datos de GPS: ${conDatosGPS.toString()}")
            } else {
                Log.i("Cuidarnos", "No se tienen datos de GPS, se intentara capturarlos")
                //lifecycleScope.launch {
                //    withContext(Dispatchers.IO) {
                        GpsUtils(this@MainActivity).turnGPSOn(GpsUtils.onGpsListener { isGPSEnable -> isGPS })
                //    }
                    Log.d("Cuidarnos", "terminado encendido de GPS")
                //}
            }
            //Se verifica si ya se rgistraron los datos del usuario
            val conDatos = prefs.getString(key, "SD")
            //showAlert("Preferencias", yaRegistrado.toString())
            if(conDatos.equals("SD")){
                Variables.primeraVez = true
                Log.i("Cuidarnos", "No se tiene datos del usuario, se inicia adctivity de registro de datos personales y de antecedentes")
                val intent = Intent(this, PageOneActivity::class.java)
                intent.putExtra("ID", Variables.IDUNICO)
                startActivity(intent)
            } else {
                Variables.primeraVez = false
                Log.i("Cuidarnos", "Se tienen datos del usuario: ${conDatos.toString()}, se salta a la pantalla principal")
                cargaDatosUsuario(conDatos.toString())
                //carga datos de antecedentes
                val conAntecedentes = prefs.getString(keyAntecedentes, "SD")
                if (conAntecedentes.equals("SD")) {
                    //No se encontraron datos de antecedentes
                    Variables.primeraVez = true
                    Log.i("Cuidarnos", "No se tiene datos de antecedentes, se inicia adctivity de registro de datos personales y de antecedentes")
                    val intent = Intent(this, PageOneActivity::class.java)
                    intent.putExtra("ID", Variables.IDUNICO)
                    startActivity(intent)
                } else {
                    //Se cargan los datos de antcedentes
                    cargaDatosAntecedentes(conAntecedentes.toString())
                }
                val intent = Intent(this, AutodiagnosticoInicialActivity::class.java)
                intent.putExtra("ID", Variables.IDUNICO)
                startActivity(intent)
                //Toast.makeText(this, "Con datos", Toast.LENGTH_SHORT).show()
            }
            setTheme(R.style.AppTheme)
            //GpsUtils(this).turnGPSOn(GpsUtils.onGpsListener { isGPSEnable -> isGPS })

            //verifica datos de GPS
        } else {
            Log.d("Error", "No se tiene conexion a internet")
            showAlert("Error", "Lo sentimos, se requiere conexión a Internet para poder usar la aplicación", "OK")
        }
    }

    private fun cargaDatosAntecedentes(datosAntecedentesCadena: String): Boolean {
        return try {
            val gson = Gson()
            val datosAntecedentesJson: List<DataAntecedentes> = gson.fromJson(datosAntecedentesCadena, Array<DataAntecedentes>::class.java).toList()
            Variables.varAntecedente1 = datosAntecedentesJson[0].antecedente1
            Variables.varAntecedente2 = datosAntecedentesJson[0].antecedente2
            Variables.varAntecedente3 = datosAntecedentesJson[0].antecedente3
            Variables.varAntecedente4 = datosAntecedentesJson[0].antecedente4
            Variables.varAntecedente5 = datosAntecedentesJson[0].antecedente5
            Variables.varAntecedente6 = datosAntecedentesJson[0].antecedente6
            Variables.varAntecedente7 = datosAntecedentesJson[0].antecedente7
            Variables.varAntecedente8 = datosAntecedentesJson[0].antecedente8
            Variables.varEmbarazada = datosAntecedentesJson[0].embarazada
            Log.d("Cuidarnos", "Se cargaron los datos de antecedentes")
            //Log.d("Cuidarnos",datosAntecedentesCadena)
            true
        } catch (e: IOException){
            false
        }
    }

    private fun cargaDatosUsuario(datosUsuarioCadena: String): Boolean {
        return try {
            val gson = Gson()
            val datosUsuarioJson: List<DataUsuario> =
                gson.fromJson(datosUsuarioCadena, Array<DataUsuario>::class.java).toList()
            Variables.IDUNICO = datosUsuarioJson[0].idUnico.toString()
            Variables.NOMBRES = datosUsuarioJson[0].nombres.toString()
            Variables.APELLIDOS = datosUsuarioJson[0].apellidos.toString()
            Variables.GENERO = datosUsuarioJson[0].genero.toString()
            Variables.FNACIMIENTO = datosUsuarioJson[0].fnacimiento.toString()
            Variables.TELEFONO = datosUsuarioJson[0].telefono.toString()
            Variables.ESTCIVIL = datosUsuarioJson[0].estCivil.toString()
            Variables.MUNICIPIO = datosUsuarioJson[0].municipio.toString()
            Variables.CIUDAD = datosUsuarioJson[0].ciudad.toString()
            Variables.BARRIO = datosUsuarioJson[0].barrio.toString()
            Variables.DIRECCION = datosUsuarioJson[0].direccion.toString()
            Log.d("Cuidarnos", "Se cargaron los datos de usuario")
            true
        } catch (e: IOException){
            false
        }
    }

    private fun cargaDatosGPS(datosGPSCadena: String): Boolean{
        return try {
            val gson = Gson()
            val datosGPSJson: List<DataGPS> =
                gson.fromJson(datosGPSCadena, Array<DataGPS>::class.java).toList()
            Variables.LATITUD = datosGPSJson[0].latitud.toString()
            Variables.LONGITUD = datosGPSJson[0].longitud.toString()
            Variables.ALTITUD = datosGPSJson[0].altitud.toString()
            Variables.PRECISION = datosGPSJson[0].precision.toString()
            Variables.TIEMPO = datosGPSJson[0].tiempo.toString()
            Log.d("Cuidarnos", "Se cargaron los datos de GPS")
            true
        } catch (e: IOException){
            false
        }
    }

    private fun showAlert(
        titulo: String,
        mensaje: String,
        textoPositivo: String = "",
        textoNeutral: String = "",
        textoNegativo: String = ""
    ){
        var respuesta: String =""
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setCancelable(false)
        builder.setPositiveButton(textoPositivo){ _, _ ->
            respuesta = "+"
            moveTaskToBack(true)
            exitProcess(-1)
        }
        builder.setNegativeButton(textoNegativo){ _, _ ->
            respuesta = "-"
        }
        builder.setNeutralButton(textoNeutral){ _, _ ->
            respuesta = "*"
        }
        val dialogo = builder.create()
        dialogo.show()
    }

    private fun notification(){

        //generacion del ID unico del celular para esta instalacion de la aplicacion
        if (Variables.IDUNICO.equals("desconocido") && verificaInternet()) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                it.result?.token?.let {
                    Log.d("Cuidarnos", "El Id unico es: ${it}")
                    Variables.IDUNICO = it
                    //suscripcion a un tema
                    FirebaseMessaging.getInstance().subscribeToTopic("COVID")
                }
            }
        }

        //recuperacion de info de una push
        val url: String? = intent.getStringExtra("url")
        url?.let {
            Log.d("Cuidarnos", "Ha llegado esta informacion en un push: ${it}")
        }
    }

    private fun generarIDUnico(): Boolean{
        var generarID = false
        //generacion del ID unico del celular
        val internet = verificaInternet()
        if (Variables.IDUNICO.equals("desconocido") && internet) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                it.result?.token?.let {
                    Log.d("Cuidarnos", "Se genero el Id unico : ${it}")
                    Variables.IDUNICO = it
                    val cadena = Variables.IDUNICO
                    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = prefs.edit()
                    editor.putString(keyID, cadena)
                    editor.apply()
                    Log.d("Cuidarnos", "Se guardo localmente el Id unico")
                    generarID = true
                }
            }
        } else {
            Log.d("Cuidarnos", "No se puede generar ID unico por falta de conexion a internet")
            generarID = false
        }
        return generarID
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun verificaInternet(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

    fun isOnlinePing(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    fun isOnline(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null &&
                cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Variables.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
            }
        }
    }




}