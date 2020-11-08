package org.csrabolivia.cuidarnos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import kotlinx.android.synthetic.main.activity_page_one.*
import org.csrabolivia.cuidarnos.R
import org.csrabolivia.cuidarnos.jsondata.Variables
import java.util.*
import kotlin.system.exitProcess

class PageOneActivity : AppCompatActivity() {

    lateinit var datePicker: DatePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_one)

        datePicker = DatePickerHelper(this)
        btContinuar1.setOnClickListener(this::escucharBtnSiguiente)
        textFieldDateBirthday.editText?.setOnClickListener(this::escucharTvFecha)

        val itemsGender = listOf("Masculino", "Femenino" )
        val adapterGender = ArrayAdapter(this, R.layout.list_item, itemsGender)
        (textFieldGender.editText as? AutoCompleteTextView)?.setAdapter(adapterGender)


        //textFieldGender.addOnEditTextAttachedListener { Variables.varEmbarazada = null }



        val itemsEstadoCivil = listOf(
            "Soltero(a)",
            "Casado(a)",
            "Conviviente",
            "Divorciado(a)",
            "Separado(a)",
            "Viudo(a)"
        )
        val adapterEstadoCivil = ArrayAdapter(this, R.layout.list_item, itemsEstadoCivil)
        (textFieldCivilState.editText as? AutoCompleteTextView)?.setAdapter(adapterEstadoCivil)

        textFieldGender.editText?.setOnClickListener(){
            closeKeyBoard()
        }

        textFieldCivilState.editText?.setOnClickListener() {
            closeKeyBoard()
        }
        textViewConfidencial.requestFocus()
        closeKeyBoard()

        if (!Variables.primeraVez) {
            textFieldNames.editText?.setText(Variables.NOMBRES)
            textFieldLastNames.editText?.setText(Variables.APELLIDOS)
            textFieldDateBirthday.editText?.setText(Variables.FNACIMIENTO)
            textoGenero.setText(Variables.GENERO,false)
            textoEstadoCivil.setText(Variables.ESTCIVIL,false)
            textFieldPhone.editText?.setText(Variables.TELEFONO)
        }
    }

    private fun escucharBtnSiguiente(v: View) {
        textFieldNames.error= null
        textFieldLastNames.error = null
        textFieldGender.error = null
        textFieldDateBirthday.error =  null
        textFieldPhone.error = null
        textFieldCivilState.error = null
        closeKeyBoard()
        if(validarCampos(textFieldNames.editText?.text.toString())) {
            textFieldNames.error = "Valor requerido"
            textFieldNames.requestFocus()

        } else if(validarCampos(textFieldLastNames.editText?.text.toString())) {
            textFieldLastNames.error = "Valor requerido"
            textFieldLastNames.requestFocus()
        } else if(validarCampos(textFieldDateBirthday.editText?.text.toString())) {
            textFieldDateBirthday.error = "Valor requerido"
            textFieldDateBirthday.requestFocus()
        } else if (validarCampos(textFieldGender.editText?.text.toString())){
            textFieldGender.error = "Valor requerido"
            textFieldGender.requestFocus()
        } else if (validarCampos(textFieldCivilState.editText?.text.toString())) {
            textFieldCivilState.error = "Valor requerido"
            textFieldCivilState.requestFocus()
        } else  if (validarCampos(textFieldPhone.editText?.text.toString())) {
            textFieldPhone.error = "Valor requerido"
            textFieldPhone.requestFocus()
        } else {
            Variables.NOMBRES = textFieldNames.editText?.text.toString()
            Variables.APELLIDOS = textFieldLastNames.editText?.text.toString()
            Variables.GENERO = textFieldGender.editText?.text.toString()
            Variables.ESTCIVIL = textFieldCivilState.editText?.text.toString()
            Variables.TELEFONO = textFieldPhone.editText?.text.toString()
            val intent = Intent(this, PageTwoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (Variables.primeraVez) {
            super.onBackPressed()
            moveTaskToBack(true)
            exitProcess(-1)
        } else {
            val intent = Intent(this, AccesoAplicacionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validarCampos(cadenaValidacion:String):Boolean{
        return cadenaValidacion.trim().isEmpty()
    }

    private fun escucharTvFecha(v: View) {
        closeKeyBoard()
        showDatePickerDialog()
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                val fechaConcatenada = "${dayStr}-${monthStr}-${year}"
                Variables.FNACIMIENTO = fechaConcatenada
                textFieldDateBirthday.editText?.setText(fechaConcatenada)
            }
        })
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