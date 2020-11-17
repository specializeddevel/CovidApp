package org.csrabolivia.cuidarnos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p1.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p1.view.*
import kotlinx.android.synthetic.main.activity_page_one.*
import org.csrabolivia.cuidarnos.R
import org.csrabolivia.cuidarnos.jsondata.DataDiagnostico
import org.csrabolivia.cuidarnos.jsondata.Variables
import www.sanju.motiontoast.MotionToast
import java.util.*

class AutodiagnosticoPeligrosP1Activity : AppCompatActivity() {

    lateinit var datePicker: DatePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autodiagnostico_peligros_p1)

        datePicker = DatePickerHelper(this, isSpinnerType = false)
        DataDiagnostico.peligroDificuldadRespiratoria = null
        DataDiagnostico.peligroSeveridadDificultadRespiratoria = null
        DataDiagnostico.peligroTiempoDificultadRespiratoria = null

        layoutADP1_2.visibility = View.INVISIBLE
        layoutADP1_3.visibility = View.INVISIBLE

        val today: Long?
        val singleDateBuilder = MaterialDatePicker.Builder.datePicker()
        today = MaterialDatePicker.todayInUtcMilliseconds()
        singleDateBuilder.setTitleText(R.string.select_date)
        singleDateBuilder.setSelection(today)
        val constru = CalendarConstraints.Builder()
                //Solo permite fechas como maximo de 3 meses
            .setStart(today!!-(2629743L*1000L)*3L)
            .setEnd(today!!)
            .setValidator(DateValidatorPointBackward.now())
        //.setValidator(DateValidatorPointForward.from(today!!-31556926L*1000L))
        singleDateBuilder.setCalendarConstraints(constru.build())
        val picker = singleDateBuilder.build()

        //tiene diificultad respiratoria
        ADPp1_1.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (toggleButton.btADPP1_1Si.isPressed) {
                    layoutADP1_2.visibility = View.VISIBLE
                    layoutADP1_3.visibility = View.VISIBLE
                    DataDiagnostico.peligroDificuldadRespiratoria  = 1
                } else {
                    //limpia y reinicializa los controles
                    ADPp1_3.clearChecked()
                    tvFechaDifResp.setText("")
                    layoutADP1_2.background = resources.getDrawable(R.drawable.border)
                    layoutADP1_3.background = resources.getDrawable(R.drawable.border)
                    DataDiagnostico.peligroTiempoDificultadRespiratoria = null
                    DataDiagnostico.peligroSeveridadDificultadRespiratoria = null
                    layoutADP1_2.visibility = View.INVISIBLE
                    layoutADP1_3.visibility = View.INVISIBLE
                    DataDiagnostico.peligroDificuldadRespiratoria = 0
                    //btADPContinuar3.callOnClick()
                }
            }
        }

        //Desde hace cuanto tiempo tiene diificultad respiratoria
        btADPSelFecha.setOnClickListener() {
            picker.show(supportFragmentManager,"DATE")
            //if(DataDiagnostico.peligroSeveridadDificultadRespiratoria!=null && DataDiagnostico.peligroTiempoDificultadRespiratoria !=null){
            //    btADPContinuar3.callOnClick()
            //}
        }
        //Que tan severa es su dificultad respiratoria
        ADPp1_3.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (toggleButton.btADPP1_3MuySevera.isPressed) {
                    DataDiagnostico.peligroSeveridadDificultadRespiratoria = 4
                } else if (toggleButton.btADPP1_3Severa.isPressed) {
                    DataDiagnostico.peligroSeveridadDificultadRespiratoria = 3
                } else if (toggleButton.btADPP1_3Moderada.isPressed) {
                    DataDiagnostico.peligroSeveridadDificultadRespiratoria = 2
                } else if (toggleButton.btADPP1_3Leve.isPressed) {
                    DataDiagnostico.peligroSeveridadDificultadRespiratoria = 1
                }
                /*if(DataDiagnostico.peligroTiempoDificultadRespiratoria!=null){
                    btADPContinuar3.callOnClick()
                }*/
            }
        }

        btADPContinuar3.setOnClickListener() {
            if (!validarRespuestas()) {
                //No paso la validacion
                MotionToast.createColorToast(
                    this, "Error!",
                    "Por favor responda todas las preguntas!",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.helvetica_regular)
                )
            } else {
                //Toast.makeText(this, "pasa a la siguiente activity", Toast.LENGTH_SHORT).show()
                //se abre la segunda activity de evaluacion de problemas mas serios de salud
                val intent = Intent(this, AutodiagnosticoPeligrosP2Activity::class.java)
                startActivity(intent)
            }
        }

        btADPAtras3.setOnClickListener(){
            onBackPressed()
        }

        picker.addOnPositiveButtonClickListener {
            val fechaSeleccionada = getDateTime(it)
            Log.d("Cuidarnos", it.toString())
            tvFechaDifResp.setText(fechaSeleccionada)
            DataDiagnostico.peligroTiempoDificultadRespiratoria = fechaSeleccionada.toString()
        }
    }

    private fun getDateTime(s: Long): String? {
        return try {
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.timeZone = TimeZone.getTimeZone("BO/bo")
            calendar.timeInMillis = s
            layoutADP1_2.background = resources.getDrawable(R.drawable.border)
            android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString()
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun validarRespuestas():Boolean {
        var exito = false
        layoutADP1_1.background = resources.getDrawable(R.drawable.border)
        layoutADP1_2.background = resources.getDrawable(R.drawable.border)
        layoutADP1_3.background = resources.getDrawable(R.drawable.border)
        if (DataDiagnostico.peligroDificuldadRespiratoria == null) {
            layoutADP1_1.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP1_1.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.peligroDificuldadRespiratoria==1 && DataDiagnostico.peligroTiempoDificultadRespiratoria == null) {
            layoutADP1_2.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP1_2.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.peligroDificuldadRespiratoria==1 && DataDiagnostico.peligroSeveridadDificultadRespiratoria == null) {
            layoutADP1_3.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP1_3.background = resources.getDrawable(R.drawable.border_red)
        } else {
            exito = true
        }
        return exito
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
                tvFechaDifResp.setText("${dayStr}-${monthStr}-${year}")
                DataDiagnostico.peligroTiempoDificultadRespiratoria = tvFechaDifResp.text.toString()
            }
        })
    }
}