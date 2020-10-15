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
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private val key = "PERSONALDATA"
    private val keyGps = "GPSDATA"
    private val keyID = "IDUNICO"
    private var isGPS = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (verificaInternet()){
            // notification()
            //btContinuar.setOnClickListener(this::escucharBtn)
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = prefs.edit()
            //editor.remove("PERSONALDATA")
            //editor.remove("IDUNICO")
            //editor.remove("GPSDATA")
            editor.apply()
            val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            val bundle = Bundle()
            bundle.putString("InitScreen", "Integración con Firebase completada")
            analytics.logEvent("InitScreen", bundle)
            //verifica datos de ID unica
            val tieneIDUnica = prefs.getString(keyID, "SD")
            if(!tieneIDUnica.equals("SD")){
                Constants.IDUNICO = tieneIDUnica.toString()
                Log.i("Cuidarnos", "Se tienen ID unica registrada: ${Constants.IDUNICO}")
            } else {
                Log.i("Cuidarnos", "No se tienen ID unica generada, se intentara generarla")
                (generarIDUnico()) /*{
                    Log.i(
                        "Cuidarnos",
                        "Parece no haber internet, no se pudo generar ID unico al llamar a la funcion de generacion"
                    )
                    showAlert(
                        "Error",
                        "Se requiere conexión a Internet para usar la aplicación",
                        "OK"
                    )
                    moveTaskToBack(true)
                    exitProcess(-1)
                    Log.i("Cuidarnos", "Salida forzosa de la aplicacion")

                }*/
            }
            val conDatosGPS = prefs.getString(keyGps, "SD")
            if(!conDatosGPS.equals("SD")){
                Constants._conDatosGPS = true
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
                Constants.primeraVez = true
                Log.i("Cuidarnos", "No se tiene datos del usuario, se inicia adctivity de registro de datos personales y de antecedentes")
                val intent = Intent(this, PageOneActivity::class.java)
                intent.putExtra("ID", Constants.IDUNICO)
                startActivity(intent)
            } else {
                Constants.primeraVez = false
                Log.i("Cuidarnos", "Se tienen datos del usuario: ${conDatos.toString()}, se salta a la pantalla principal")
                //val intent = Intent(this, AccesoAplicacionActivity::class.java)
                val intent = Intent(this, AutodiagnosticoInicialActivity::class.java)
                intent.putExtra("ID", Constants.IDUNICO)
                startActivity(intent)
                Toast.makeText(this, "Con datos", Toast.LENGTH_SHORT).show()
            }
            setTheme(R.style.AppTheme)
            //GpsUtils(this).turnGPSOn(GpsUtils.onGpsListener { isGPSEnable -> isGPS })

            //verifica datos de GPS
        } else {
            Log.d("Error", "No se tiene conexion a internet")
            showAlert("Error", "Lo sentimos, se requiere conexión a Internet para poder usar la aplicación", "OK")
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

        //generacion del ID unico del celular
        if (Constants.IDUNICO.equals("desconocido") && verificaInternet()) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {

                it.result?.token?.let {
                    Log.d("Cuidarnos", "El Id unico es: ${it}")
                    Constants.IDUNICO = it
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
        if (Constants.IDUNICO.equals("desconocido") && internet) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                it.result?.token?.let {
                    Log.d("Cuidarnos", "Se genero el Id unico : ${it}")
                    Constants.IDUNICO = it
                    val cadena = Constants.IDUNICO
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
            if (requestCode == Constants.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
            }
        }
    }




}