package org.csrabolivia.covidapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_autodiagnostico_molestias.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_molestias.view.*
import kotlinx.android.synthetic.main.activity_page_one.*
import java.io.IOException

class AutodiagnosticoMolestiasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autodiagnostico_molestias)

        DataDiagnostico.tosSeca = null
        DataDiagnostico.fiebre = null
        DataDiagnostico.malestar = null
        DataDiagnostico.dolorCabeza = null
        DataDiagnostico.dificultadRespirar = null
        DataDiagnostico.dolorMuscular = null
        DataDiagnostico.dolorGarganta = null
        DataDiagnostico.perdidaOlfato = null
        DataDiagnostico.perdidaGusto = null

        //tos seca
        ADMp1.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.tosSeca = if (toggleButton.btADMP1Si.isPressed) 1 else 0
            }
        }
        //fiebre
        ADMp2.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.fiebre = if (toggleButton.btADMP2Si.isPressed) 1 else 0
            }
        }
        //malestar general
        ADMp3.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.malestar = if (toggleButton.btADMP3Si.isPressed) 1 else 0
            }
        }
        //Dolor de cabeza
        ADMp4.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.dolorCabeza = if (toggleButton.btADMP4Si.isPressed) 1 else 0
            }
        }
        //Dificultad para respirar
        ADMp5.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.dificultadRespirar = if (toggleButton.btADMP5Si.isPressed) 1 else 0
            }
        }
        //Dolor muscular
        ADMp6.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.dolorMuscular = if (toggleButton.btADMP6Si.isPressed) 1 else 0
            }
        }
        //Dolor de garganta
        ADMp7.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.dolorGarganta = if (toggleButton.btADMP7Si.isPressed) 1 else 0
            }
        }
        //Perdida de olfato
        ADMp8.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.perdidaOlfato = if (toggleButton.btADMP8Si.isPressed) 1 else 0
            }
        }
        //perdida del gusto
        ADMp9.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.perdidaGusto = if (toggleButton.btADMP9Si.isPressed) 1 else 0
            }
        }

        btADMContinuar2.setOnClickListener() {
            if (!validarRespuestas()) {
                //No paso la validacion
                Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val sumaMolestias =
                    DataDiagnostico.tosSeca!! + DataDiagnostico.fiebre!! + DataDiagnostico.malestar!! + DataDiagnostico.dolorCabeza!! + DataDiagnostico.dificultadRespirar!! + DataDiagnostico.dolorMuscular!! + DataDiagnostico.dolorGarganta!! + DataDiagnostico.perdidaOlfato!! + DataDiagnostico.perdidaGusto!!
                if (sumaMolestias == 0) {
                    //No tiene ningun molestia
                    Toast.makeText(this, "Tiene contacto covid: ${DataDiagnostico.tieneContactoCovid}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MensajeAutodiagnosticoActivity::class.java)
                    if (DataDiagnostico.tieneContactoCovid != 1)
                        DataDiagnostico.nivelDeRieso = 0 //No tiene molestias ni tuvo contacto con una persona con o sospechosa de covid
                    else {
                        DataDiagnostico.nivelDeRieso = 1 //No tiene molestias pero tuvo contacto con una persona con o sospechosa de covid
                    }
                    startActivity(intent)
                } else {
                    val intent = Intent(this, AutodiagnosticoPeligrosP1Activity::class.java)
                    startActivity(intent)
                }
            }
        }

        btADMAtras2.setOnClickListener(){
            onBackPressed()
        }
    }

    fun validarRespuestas():Boolean {
        var exito = false
        layoutADM1.background = resources.getDrawable(R.drawable.border)
        layoutADM2.background = resources.getDrawable(R.drawable.border)
        layoutADM3.background = resources.getDrawable(R.drawable.border)
        layoutADM4.background = resources.getDrawable(R.drawable.border)
        layoutADM5.background = resources.getDrawable(R.drawable.border)
        layoutADM6.background = resources.getDrawable(R.drawable.border)
        layoutADM7.background = resources.getDrawable(R.drawable.border)
        layoutADM8.background = resources.getDrawable(R.drawable.border)
        layoutADM9.background = resources.getDrawable(R.drawable.border)
        if (DataDiagnostico.tosSeca == null) {
            layoutADM1.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM1.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.fiebre == null) {
            layoutADM2.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM2.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.malestar == null) {
            layoutADM3.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM3.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.dolorCabeza == null) {
            layoutADM4.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM4.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.dificultadRespirar == null) {
            layoutADM5.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM5.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.dolorMuscular == null) {
            layoutADM6.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM6.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.dolorGarganta == null) {
            layoutADM7.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM7.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.perdidaOlfato == null) {
            layoutADM8.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM8.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.perdidaGusto == null) {
            layoutADM9.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADM9.background = resources.getDrawable(R.drawable.border_red)
        } else {
            exito = true
        }
        return exito
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
}