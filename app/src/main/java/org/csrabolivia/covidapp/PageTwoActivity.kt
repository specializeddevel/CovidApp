package org.csrabolivia.covidapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import kotlinx.android.synthetic.main.activity_page_one.*
import kotlinx.android.synthetic.main.activity_page_three.*
import kotlinx.android.synthetic.main.activity_page_two.*
import kotlinx.android.synthetic.main.activity_page_two.btAtras1
import kotlinx.android.synthetic.main.activity_page_two.btContinuar2
import org.json.JSONObject



class PageTwoActivity : AppCompatActivity() {

    private val key = "PERSONALDATA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_two)

        val bundle: Bundle ?=intent.extras
        val nombre = bundle!!.getString(Constants.NOMBRES)
        val apellidos = bundle!!.getString(Constants.APELLIDOS)
        val genero = bundle!!.getString(Constants.GENERO)
        val fnacimiento = bundle!!.getString(Constants.FNACIMIENTO)
        val telefono = bundle!!.getString(Constants.TELEFONO)
        val estCivil = bundle!!.getString(Constants.ESTCIVIL)
        val municipio = bundle!!.getString(Constants.MUNICIPIO)
        val ciudad = bundle!!.getString(Constants.CIUDAD)

        val itemsMunicipio = listOf("Montero", "Otro")
        val adapterMunicipio = ArrayAdapter(this, R.layout.list_item, itemsMunicipio)
        (textFieldMunicipio.editText as? AutoCompleteTextView)?.setAdapter(adapterMunicipio)

        val itemsCiudad = listOf("Montero", "Otro")
        val adapterCiudad = ArrayAdapter(this, R.layout.list_item, itemsCiudad)
        (textFieldCiudad.editText as? AutoCompleteTextView)?.setAdapter(adapterCiudad)

        val itemsBarrio = listOf("24 de septiembre","27 de mayo","3 de Mayo","30 de Noviembre","Asahi","Bolívar","El Carmen","Fabril","Kennedy","La Cruz","Monteverde","San Juan XXIII","Santa Barbara","Santa Tereza","Villa Cbba.","Villa Verde","Villa Virginia","Virgen de Cotoca","Zona Central","Otro...")
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

        textFieldBarrio.editText?.setOnClickListener(){
            closeKeyBoard()
        }

        btContinuar2.setOnClickListener() {
            textFieldMunicipio.error = null
            textFieldCiudad.error = null
            textFieldBarrio.error = null
            textFieldDireccion.error = null
            if (validarCampos(textFieldMunicipio.editText?.text.toString())) {
                textFieldMunicipio.error = "Valor requerido"
            } else if (validarCampos(textFieldCiudad.editText?.text.toString())) {
                textFieldCiudad.error = "Valor requerido"
            } else  if (validarCampos(textFieldBarrio.editText?.text.toString())) {
                textFieldBarrio.error = "Valor requerido"
            } else if (validarCampos(textFieldDireccion.editText?.text.toString())) {
                textFieldDireccion.error = "Valor requerido"
                textFieldDireccion.requestFocus()
                //penKeyBoard()
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
                //Log.d("Depuracion", cadena)
                //var intent = Intent(this, PageThreeActivity::class.java)
                //startActivity(intent)
                //se guardan los datos del usuario
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = prefs.edit()
                editor.putString(key,cadena)
                editor.apply()
                var intent = Intent(this, PageThreeActivity::class.java)
                startActivity(intent)
            }

        }
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
}