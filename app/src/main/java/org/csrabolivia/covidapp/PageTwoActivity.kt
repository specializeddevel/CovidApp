package org.csrabolivia.covidapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_page_two.*
import kotlinx.android.synthetic.main.activity_page_two.btAtras1
import kotlinx.android.synthetic.main.activity_page_two.btContinuar2
import kotlinx.android.synthetic.main.activity_page_two.view.*
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_page_one.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread


class PageTwoActivity : AppCompatActivity() {

    private val key = "PERSONALDATA"
    private val keyGps = "GPSDATA"
    private var cordenadas: String =""
    private val GPS1_REQUEST_CODE = 101
    private val GPS2_REQUEST_CODE = 102
    private val TAG = "Permisos"
    private var isGPS = false
    private var respuestaDialogo: String = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_two)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this!!)

        val bundle: Bundle? = intent.extras
        val nombre = bundle!!.getString(Constants.NOMBRES)
        val apellidos = bundle!!.getString(Constants.APELLIDOS)
        val genero = bundle!!.getString(Constants.GENERO)
        val fnacimiento = bundle!!.getString(Constants.FNACIMIENTO)
        val telefono = bundle!!.getString(Constants.TELEFONO)
        val estCivil = bundle!!.getString(Constants.ESTCIVIL)
        val municipio = bundle!!.getString(Constants.MUNICIPIO)
        val ciudad = bundle!!.getString(Constants.CIUDAD)

        var seleccionEnCasa: Int? = null

        val itemsMunicipio = listOf("Montero", "Otro")
        val adapterMunicipio = ArrayAdapter(this, R.layout.list_item, itemsMunicipio)
        (textFieldMunicipio.editText as? AutoCompleteTextView)?.setAdapter(adapterMunicipio)

        val itemsCiudad = listOf("Montero", "Otro")
        val adapterCiudad = ArrayAdapter(this, R.layout.list_item, itemsCiudad)
        (textFieldCiudad.editText as? AutoCompleteTextView)?.setAdapter(adapterCiudad)

        val itemsBarrio = listOf(
            "24 de septiembre",
            "27 de mayo",
            "3 de Mayo",
            "30 de Noviembre",
            "Asahi",
            "Bolívar",
            "El Carmen",
            "Fabril",
            "Kennedy",
            "La Cruz",
            "Monteverde",
            "San Juan XXIII",
            "Santa Barbara",
            "Santa Tereza",
            "Villa Cbba.",
            "Villa Verde",
            "Villa Virginia",
            "Virgen de Cotoca",
            "Zona Central",
            "Otro..."
        )
        val adapterBarrio = ArrayAdapter(this, R.layout.list_item, itemsBarrio)
        (textFieldBarrio.editText as? AutoCompleteTextView)?.setAdapter(adapterBarrio)

        btAtras1.setOnClickListener() {
            onBackPressed()
        }

        textFieldMunicipio.editText?.setOnClickListener() {
            closeKeyBoard()
        }

        textFieldCiudad.editText?.setOnClickListener() {
            closeKeyBoard()
        }

        textFieldBarrio.editText?.setOnClickListener() {
            closeKeyBoard()
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                GpsUtils(this@PageTwoActivity).turnGPSOn(GpsUtils.onGpsListener { isGPSEnable -> isGPS })
            }
            Log.d(TAG, "terminado encendido de GPS")
        }

        tbEnCasa.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->

            if(isChecked){
                if (toggleButton.buttonEnCasaSi.isPressed){
                    seleccionEnCasa = 1
                    if (setupPermissions()) {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                getLastKnownLocation()
                            }
                        }
                    }
                }
                else{
                    seleccionEnCasa = 0
                }
            }
        }

        btContinuar2.setOnClickListener() {
            textFieldMunicipio.error = null
            textFieldCiudad.error = null
            textFieldBarrio.error = null
            textFieldDireccion.error = null
            layoutEnCasa.background = resources.getDrawable(R.drawable.border)
            closeKeyBoard()
            if (validarCampos(textFieldMunicipio.editText?.text.toString())) {
                textFieldMunicipio.error = "Valor requerido"
            } else if (validarCampos(textFieldCiudad.editText?.text.toString())) {
                textFieldCiudad.error = "Valor requerido"
            } else if (validarCampos(textFieldBarrio.editText?.text.toString())) {
                textFieldBarrio.error = "Valor requerido"
            } else if (validarCampos(textFieldDireccion.editText?.text.toString())) {
                textFieldDireccion.error = "Valor requerido"
                textFieldDireccion.requestFocus()
                //penKeyBoard()
            } else if (seleccionEnCasa == null) {
                tbEnCasa.buttonEnCasaSi.requestFocus()
                layoutEnCasa.setBackgroundColor(Color.parseColor("#FFCDD2"))
                layoutEnCasa.background = resources.getDrawable(R.drawable.border_red)
                Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_LONG)
                    .show()
            } else {
                val REGISTRO = JSONObject()
                REGISTRO.put("idUnico", Constants.IDUNICO)
                REGISTRO.put("nombres", nombre)
                REGISTRO.put("apellidos", apellidos)
                REGISTRO.put("genero", genero)
                REGISTRO.put("fnacimiento", fnacimiento)
                REGISTRO.put("telefono", telefono)
                REGISTRO.put("estCivil", estCivil)
                REGISTRO.put("municipio", municipio)
                REGISTRO.put("ciudad", ciudad)
                REGISTRO.put("barrio", textFieldBarrio.editText?.text.toString())
                REGISTRO.put("direccion", textFieldDireccion.editText?.text.toString())
                val cadena: String = REGISTRO.toString()
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = prefs.edit()
                editor.putString(key, cadena)
                editor.apply()
                val conDatos = prefs.getString(key, "SD")
                Log.d(TAG, conDatos.toString())
                val intent = Intent(this, AntecedentesActivity::class.java)
                intent.putExtra(Constants.GENERO, genero)
                startActivity(intent)
            }

        }


    }


    private fun showAlert(titulo: String, mensaje: String): AlertDialog{
        val mAlertDialogBuilder = AlertDialog.Builder(this)
        mAlertDialogBuilder.setTitle(titulo)
        mAlertDialogBuilder.setMessage(mensaje)
        mAlertDialogBuilder.setCancelable(false)
        mAlertDialogBuilder.setPositiveButton("Si"){_,_->
           respuestaDialogo="si"
        }
        mAlertDialogBuilder.setNegativeButton("No"){_,_->
            respuestaDialogo="no"
        }
        val mAlertDialog = mAlertDialogBuilder.create()
        return mAlertDialog
    }

    private fun validarCampos(cadenaValidacion:String):Boolean{
        return cadenaValidacion.trim().isEmpty()
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun openKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }

    private fun setupPermissions(): Boolean  {
        var tienePermisos: Boolean = false
        val permissionGPS1 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        val permissionGPS2 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionGPS1 != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to GPS1 denied")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                GPS1_REQUEST_CODE)
        } else if (permissionGPS2 != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to GPS1 denied")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                GPS2_REQUEST_CODE)
        } else {
            Log.d(TAG, "Se tienen los permisos para el GPS")
            tienePermisos = true
        }
        return tienePermisos
    }

    fun getLastKnownLocation(): Boolean {
        var exito = false
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Thread.sleep(2000)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val COORDENADASGPS = JSONObject()
                        COORDENADASGPS.put("latitud", location.latitude)
                        COORDENADASGPS.put("longitud", location.longitude)
                        val cadenaGps: String = COORDENADASGPS.toString()
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@PageTwoActivity)
                        val editor = prefs.edit()
                        editor.putString(keyGps, cadenaGps)
                        editor.apply()
                        Log.d(TAG, "valor location ${location.toString()}")
                        exito = true
                    } else {
                        Log.d(TAG, "Error al obtenr valor location ${location.toString()}")
                        Toast.makeText(
                            this,
                            "No se pudieron obtener las coordenadas GPS",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
        return exito
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray){

        when (requestCode) {
            GPS1_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
            GPS2_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }




}