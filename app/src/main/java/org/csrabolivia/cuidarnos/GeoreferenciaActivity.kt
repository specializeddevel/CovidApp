package org.csrabolivia.cuidarnos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.likethesalad.android.aaper.api.EnsurePermissions
import kotlinx.android.synthetic.main.activity_antecedentes.*
import kotlinx.android.synthetic.main.activity_georeferencia.*
import kotlinx.android.synthetic.main.activity_page_two.tbEnCasa
import kotlinx.android.synthetic.main.activity_page_two.view.buttonEnCasaSi
import org.csrabolivia.cuidarnos.jsondata.Variables
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast

class GeoreferenciaActivity : AppCompatActivity() {

    private val keyGps = "GPSDATA"
    var seleccionEnCasa: Int? = null
    private val GPS2_REQUEST_CODE = 102
    private var isGPS = false

    private val db = FirebaseFirestore.getInstance()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_georeferencia)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this!!)

        btEntrar.setOnClickListener(){
            if(!validarRespuestas()){
                //No paso la validacion
                MotionToast.createColorToast(
                    this, "Error!",
                    "Por favor responda todas las preguntas!",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.helvetica_regular)
                )
            }else{
                    finish()
            }

        }



        tbEnCasa.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->

            if (isChecked) {
                if (toggleButton.buttonEnCasaSi.isPressed) {
                    seleccionEnCasa = 1
                    getLastKnownLocation()
                } else {
                    seleccionEnCasa = 0

                }
            }
        }

        GpsUtils(this).turnGPSOn(GpsUtils.onGpsListener { isGPSEnable -> isGPS })

    }


    fun validarRespuestas():Boolean {
        var exito = false
        layoutEnCasaIndependiente.background = resources.getDrawable(R.drawable.border)
        if (seleccionEnCasa == null) {
            layoutEnCasaIndependiente.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutEnCasaIndependiente.background = resources.getDrawable(R.drawable.border_red)
        } else {
            exito = true
        }
        return exito
    }

    private fun setupPermissions(): Boolean  {
        val permissionGPS2 = ContextCompat.checkSelfPermission(
            this,
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
        //return getLastKnownLocation()
        return true
        //    }
        //}

    }

    @EnsurePermissions(permissions = [Manifest.permission.ACCESS_FINE_LOCATION])
    fun getLastKnownLocation() {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Thread.sleep(2000)
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
                            val prefs =
                                PreferenceManager.getDefaultSharedPreferences(this@GeoreferenciaActivity)
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
                            if (!Variables.IDUNICO.equals("desconocido")) {
                                db.collection("usuarios").document(Variables.IDUNICO).set(
                                    hashMapOf
                                        (
                                        "latitud" to Variables.LATITUD,
                                        "longitud" to Variables.LONGITUD,
                                        "altura" to Variables.ALTITUD,
                                        "precision" to Variables.PRECISION,
                                        "tiempo" to Variables.TIEMPO
                                    ), SetOptions.merge()
                                )
                            }
                            Log.i(
                                "Cuidarnos",
                                "Se registraron los datos del usuario en la nube"
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

                }
            }
        }
    }

}