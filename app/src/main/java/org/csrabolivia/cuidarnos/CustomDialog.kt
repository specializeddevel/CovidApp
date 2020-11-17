package org.csrabolivia.cuidarnos

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.csrabolivia.cuidarnos.jsondata.Variables
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast

private val GPS2_REQUEST_CODE = 102
private var isGPS = false
private lateinit var fusedLocationClient: FusedLocationProviderClient
private val keyGps = "GPSDATA"

class CustomDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_dialog)

    }

    fun dialogito(contexto: Context?) {
        val dialog = Dialog(contexto!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.activity_custom_dialog_fragment)
        dialog.setCancelable(false)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.findViewById<View>(R.id.custom_dialog_btn_cancel)
            .setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.custom_dialog_btn_submit).setOnClickListener {
            setupPermissions(contexto)
            //Toast.makeText(contexto, "PResiono SI", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
        dialog.window!!.attributes = layoutParams
    }

    private fun setupPermissions(contexto: Context): Boolean  {

        val permissionGPS2 = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        when {
            permissionGPS2 != PackageManager.PERMISSION_GRANTED -> {
                Log.i("Cuidarnos", "Permission to GPS2 denied")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    GPS2_REQUEST_CODE
                )
                Log.d(
                    "Cuidarnos",
                    "Permiso no concedido anteriormente, Se intenta otorgar permisos para el GPS"
                )
            }
            else -> {
                Log.d("Cuidarnos", "Ya se tenian los permisos para el GPS")
            }

        }
        //lifecycleScope.launch {
        //    withContext(Dispatchers.IO) {
        return getLastKnownLocation(contexto)
        //    }
        //}

    }

    fun getLastKnownLocation(contexto: Context): Boolean {
        var exito = false
        if (ActivityCompat.checkSelfPermission(
                contexto, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            //Thread.sleep(2000)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(contexto)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val COORDENADASGPS = JSONObject()
                        COORDENADASGPS.put("latitud", location.latitude)
                        COORDENADASGPS.put("longitud", location.longitude)
                        COORDENADASGPS.put("altitud", location.altitude)
                        COORDENADASGPS.put("precision", location.accuracy)
                        COORDENADASGPS.put("tiempo", location.time)
                        val cadenaGps = "[ $COORDENADASGPS ]"
                        val prefs = PreferenceManager.getDefaultSharedPreferences(contexto)
                        val editor = prefs.edit()
                        editor.putString(keyGps, cadenaGps)
                        editor.apply()
                        Variables.LATITUD = location.latitude.toString()
                        Variables.LONGITUD = location.longitude.toString()
                        Variables.ALTITUD = location.altitude.toString()
                        Variables.PRECISION = location.accuracy.toString()
                        Variables.TIEMPO = location.time.toString()
                        Log.d(
                            "Cuidarnos",
                            "Se capturaron y almacenarón los siguientes datos de GPS: $cadenaGps"
                        )
                        MotionToast.createColorToast(
                            this, "Información",
                            "Posición geográfica almacenada correctamente",
                            MotionToast.TOAST_INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, R.font.helvetica_regular)
                        )
                    } else {
                        Log.d("Cuidarnos", "Error al obtener valor de location")
                    }
                }
        }
        return exito
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Variables.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
            }  
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ){

        when (requestCode) {
            GPS2_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("Cuidarnos", "Permiso denegado por el usuario")
                } else {
                    Log.i("Cuidarnos", "Permiso concedido por el usuario")
                    getLastKnownLocation(this)
                }
            }
        }
    }

}