package org.csrabolivia.cuidarnos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_page_one.*
import org.csrabolivia.cuidarnos.jsondata.Variables
import java.util.*
import kotlin.system.exitProcess
import org.csrabolivia.cuidarnos.utiles.InputFilterMinMax
import java.util.regex.Pattern


class PageOneActivity : AppCompatActivity() {

    //val REG = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{8}\$"
    //var PATTERN: Pattern = Pattern.compile(REG)

    //fun CharSequence.isPhoneNumber() : Boolean = PATTERN.matcher(this).find()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_one)



        btContinuar1.setOnClickListener(this::escucharBtnSiguiente)
        //textFieldDateBirthday.editText?.setOnClickListener(this::escucharTvFecha)
        val today: Long?
        val singleDateBuilder = MaterialDatePicker.Builder.datePicker()
        today = MaterialDatePicker.todayInUtcMilliseconds()
        singleDateBuilder.setTitleText(R.string.select_date)
        singleDateBuilder.setSelection(today)
        val constru = CalendarConstraints.Builder()
            .setStart(today!!-(31556926L*1000L)*95L)
            .setEnd(today!!)
            .setValidator(DateValidatorPointBackward.now())
            //.setValidator(DateValidatorPointForward.from(today!!-31556926L*1000L))
        singleDateBuilder.setCalendarConstraints(constru.build())
        val picker = singleDateBuilder.build()

        val itemsGender = listOf("Masculino", "Femenino")
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
            textoGenero.setText(Variables.GENERO, false)
            textoEstadoCivil.setText(Variables.ESTCIVIL, false)
            textFieldPhone.editText?.setText(Variables.TELEFONO)
        }

        picker.addOnPositiveButtonClickListener {
            val fechaSeleccionada = getDateTime(it)
            Log.d("Cuidarnos", it.toString())
            Variables.FNACIMIENTO = fechaSeleccionada.toString()
            textFieldDateBirthday.editText?.setText(fechaSeleccionada.toString())
        }

        textFieldDateBirthday.editText?.setOnClickListener() {
            closeKeyBoard()
            //showDatePickerDialog()
            picker.show(supportFragmentManager,"DATE")
        }

        textFieldPhone.editText?.setFilters(arrayOf<InputFilter>(InputFilterMinMax(0,79999999)))

    }

    private fun getDateTime(s: Long): String? {
        return try {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.timeZone = TimeZone.getTimeZone("BO/bo")
            calendar.timeInMillis = s
            android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString()
        } catch (e: Exception) {
            e.toString()
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

        val numeroTelefono: Int


        numeroTelefono = if (textFieldPhone.editText?.text.toString().isEmpty()){
            0
        } else {
            (textFieldPhone.editText?.text.toString().toInt())
        }

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
        } else  if (textFieldPhone.editText?.text.toString().trim().isEmpty()) {
            textFieldPhone.error = "Valor requerido"
            textFieldPhone.requestFocus()
        } else if (numeroTelefono < 60000000 || numeroTelefono > 79999999) {
            textFieldPhone.error = "Número de teléfono celular invalido"
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
            //moveTaskToBack(true)
            //exitProcess(-1)
            finishAffinity()
        } else {
            val intent = Intent(this, AccesoAplicacionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validarCampos(cadenaValidacion: String):Boolean{
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