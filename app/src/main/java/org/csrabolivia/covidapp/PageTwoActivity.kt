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
import kotlinx.android.synthetic.main.activity_page_two.btFinalizar1
import kotlinx.android.synthetic.main.activity_page_two.view.*
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_page_one.*
import kotlinx.android.synthetic.main.activity_page_three.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.concurrent.thread


class PageTwoActivity : AppCompatActivity() {

    private val key = "PERSONALDATA"
    private val keyGps = "GPSDATA"
    private val keyID = "IDUNICO"
    private val GPS2_REQUEST_CODE = 102
    private var isGPS = false
    private var respuestaDialogo: String = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val db = FirebaseFirestore.getInstance()

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

        tbEnCasa.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->

            if (isChecked) {
                if (toggleButton.buttonEnCasaSi.isPressed) {
                    seleccionEnCasa = 1
                    setupPermissions()
                } else {
                    seleccionEnCasa = 0
                }
            }
        }

        checkBoxTerminos.setOnClickListener(){
            closeKeyBoard()
            btFinalizar1.isEnabled = checkBoxTerminos.isChecked
        }


        btFinalizar1.setOnClickListener() {
                textFieldMunicipio.error = null
                textFieldCiudad.error = null
                textFieldBarrio.error = null
                textFieldDireccion.error = null
                layoutEnCasa.background = resources.getDrawable(R.drawable.border)
                closeKeyBoard()
                val barrio = textFieldBarrio.editText?.text.toString()
                val direccion = textFieldDireccion.editText?.text.toString()
                val municipio = textFieldMunicipio.editText?.text.toString()
                val ciudad = textFieldCiudad.editText?.text.toString()
                if (validarCampos(municipio)) {
                    textFieldMunicipio.error = "Valor requerido"
                } else if (validarCampos(ciudad)) {
                    textFieldCiudad.error = "Valor requerido"
                } else if (validarCampos(barrio)) {
                    textFieldBarrio.error = "Valor requerido"
                } else if (validarCampos(direccion)) {
                    textFieldDireccion.error = "Valor requerido"
                    textFieldDireccion.requestFocus()
                    //penKeyBoard()
                } else if (seleccionEnCasa == null) {
                    tbEnCasa.buttonEnCasaSi.requestFocus()
                    layoutEnCasa.setBackgroundColor(Color.parseColor("#FFCDD2"))
                    layoutEnCasa.background = resources.getDrawable(R.drawable.border_red)
                    Toast.makeText(
                        this,
                        "Por favor responda todas las preguntas",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    if (!verificaInternet()) {
                        Toast.makeText(this, "No hay internet, no se puede continuar", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        //Guardado de los datos del usaurio localmente mediante shared preferences
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
                        REGISTRO.put("barrio", barrio)
                        REGISTRO.put("direccion", direccion)
                        val cadena: String = REGISTRO.toString()
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = prefs.edit()
                        editor.putString(key, cadena)
                        editor.apply()
                        val conDatos = prefs.getString(key, "SD")
                        Log.d(
                            "Cuidarnos",
                            "Se guardaron localmente los datos del usuario: ${conDatos.toString()}"
                        )
                        //Guardar datos en la BD remota en firestore
                        //TODO: Si existieran datos ya almacenados localmente se deberian cargar al momento de cargar cada activity para el caso de edicion
                            if (!Constants.IDUNICO.equals("desconocido")) {
                                db.collection("usuarios").document(Constants.IDUNICO).set(
                                    hashMapOf
                                        (
                                        "id" to Constants.IDUNICO,
                                        "nombres" to nombre,
                                        "apellidos" to apellidos,
                                        "fnacimiento" to fnacimiento,
                                        "genero" to genero,
                                        "estCivil" to estCivil,
                                        "telefono" to telefono,
                                        "municipio" to municipio,
                                        "ciudad" to ciudad,
                                        "barrio" to barrio,
                                        "direccion" to direccion,
                                        "latitud" to Constants.LATITUD,
                                        "longitud" to Constants.LONGITUD,
                                        "altura" to Constants.ALTITUD,
                                        "precision" to Constants.PRECISION,
                                        "tiempo" to Constants.TIEMPO
                                    ), SetOptions.merge()
                                )
                                Log.i("Cuidarnos", "Se registraron los datos en la nube")
                                val intent = Intent(this, AntecedentesActivity::class.java)
                                intent.putExtra(Constants.GENERO, genero)
                                startActivity(intent)
                            } else {
                                Log.i(
                                    "Cuidarnos",
                                    "No se registraron los datos en la nube por falta de ID"
                                )
                                if (!generarIDUnico()) {
                                    Toast.makeText(
                                        this,
                                        "No se puede continuar sin una conexión a Internet, intente nuevamente",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.i("Cuidarnos", "No se pudo generar ID unico")
                                    btAtras1.callOnClick()
                                } else {
                                    btFinalizar1.callOnClick()
                                }
                            }

                    }
                }
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
        val permissionGPS2 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        when {
            permissionGPS2 != PackageManager.PERMISSION_GRANTED -> {
                Log.i("Cuidarnos", "Permission to GPS2 denied")
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    GPS2_REQUEST_CODE)
                Log.d("Cuidarnos", "Permiso no concedido anteriormente, Se intenta otorgar permisos para el GPS")
            }
            else -> {
                Log.d("Cuidarnos", "Ya se tenian los permisos para el GPS")

            }

        }
        //lifecycleScope.launch {
        //    withContext(Dispatchers.IO) {
                return getLastKnownLocation()
        //    }
        //}

    }

    fun getLastKnownLocation(): Boolean {
        var exito = false
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
                        val cadenaGps: String = COORDENADASGPS.toString()
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@PageTwoActivity)
                        val editor = prefs.edit()
                        editor.putString(keyGps, cadenaGps)
                        editor.apply()
                        Constants.LATITUD = location.latitude.toString()
                        Constants.LONGITUD = location.longitude.toString()
                        Constants.ALTITUD = location.altitude.toString()
                        Constants.PRECISION = location.accuracy.toString()
                        Constants.TIEMPO = location.time.toString()
                        Log.d("Cuidarnos", "Se capturaron los siguientes datos de GPS ${location.toString()}")
                        Toast.makeText(this, "Posición geográfica almacenada", Toast.LENGTH_SHORT).show()
                        exito = true
                    } else {
                        Log.d("Cuidarnos", "Error al obtener valor de location")
                        //Toast.makeText(this, "No se pudieron obtener las coordenadas GPS", Toast.LENGTH_SHORT).show()
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
            GPS2_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("Cuidarnos", "Permiso denegado por el usuario")
                } else {
                    Log.i("Cuidarnos", "Permiso concedido por el usuario")
                    getLastKnownLocation()
                }
            }
        }
    }




}