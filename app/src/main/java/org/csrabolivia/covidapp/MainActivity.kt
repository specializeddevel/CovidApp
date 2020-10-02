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
    private var isGPS = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isOnline()){

             notification()
            //btContinuar.setOnClickListener(this::escucharBtn)
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = prefs.edit()
            editor.remove("PERSONALDATA")
            editor.apply()
            val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
            val bundle = Bundle()
            bundle.putString("InitScreen", "Integración con Firebase completada")
            analytics.logEvent("InitScreen", bundle)
            //verifica datos de GPS
            val conDatosGPS = prefs.getString(keyGps, "SD")
            //Se verifica si ya se rgistraron los datos del usuario
            if(!conDatosGPS.equals("SD")){
                Constants._conDatosGPS = true
                Log.i("GPSData", "Se tienen datos de GPS: ${conDatosGPS.toString()}")
            }
            val conDatos = prefs.getString(key, "SD")
            //showAlert("Preferencias", yaRegistrado.toString())
            if(conDatos.equals("SD")){
                Log.i("UserData", "No se tiene datos del usuario")
                val intent = Intent(this, PageOneActivity::class.java)
                intent.putExtra("ID", Constants.IDUNICO)
                startActivity(intent)
            } else {
                Log.i("UserData", "Se tienen datos del usuario: ${conDatos.toString()}")
                val intent = Intent(this, AutodiagnosticoActivity::class.java)
                intent.putExtra("ID", Constants.IDUNICO)
                startActivity(intent)
                Toast.makeText(this, "Con datos", Toast.LENGTH_SHORT).show()
            }
            setTheme(R.style.AppTheme)
            //GpsUtils(this).turnGPSOn(GpsUtils.onGpsListener { isGPSEnable -> isGPS })
        } else {
            Log.d("Error", "No se tiene conexion a internet")
            showAlert("Error", "Se requiere conexión a Internet", "OK")
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

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener{

            it.result?.token?.let{
                Log.d("Depuracion", "El Id unico es: ${it}")
                Constants.IDUNICO = it
            }
        }
        //suscripcion a un tema
        FirebaseMessaging.getInstance().subscribeToTopic("COVID")

        //recuperacion de info de una push
        val url: String? = intent.getStringExtra("url")
        url?.let {
            Log.d("Depuracion", "Ha llegado esta informacion en un push: ${it}")
        }
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