 package org.csrabolivia.covidapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p2.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p2.view.*
import org.csrabolivia.covidapp.jsondata.DataDiagnostico

 class AutodiagnosticoPeligrosP2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autodiagnostico_peligros_p2)

        layoutADP4_2.visibility = View.GONE
        DataDiagnostico.peligroCansancioFatiga = null
        DataDiagnostico.peligroMalestarGeneral = null
        DataDiagnostico.peligroTieneOTuvoFiebre = null
        DataDiagnostico.peligroIntensidadFiebre = null
        DataDiagnostico.peligroDolorDePecho = null

        //Cansancio y fatiga
        ADPp2.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.peligroCansancioFatiga =
                    if (toggleButton.btADPP2Si.isPressed) 1 else 0
            }
        }

        //malestar general
        ADPp3.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.peligroMalestarGeneral =
                    if (toggleButton.btADPP3Si.isPressed) 1 else 0
            }
        }

        //tiene fiebre
        ADPp4_1.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (toggleButton.btADPP4_1Si.isPressed) {
                    this.layoutADP4_2.visibility = View.VISIBLE
                    DataDiagnostico.peligroTieneOTuvoFiebre = 1
                } else {
                    //limpia y reinicializa los controles de intensidad de fiebre
                    ADPp4_2.clearChecked()
                    this.layoutADP4_2.background = resources.getDrawable(R.drawable.border)
                    DataDiagnostico.peligroIntensidadFiebre = null
                    this.layoutADP4_2.visibility = View.GONE
                    DataDiagnostico.peligroTieneOTuvoFiebre = 0
                    //btADPContinuar3.callOnClick()
                }
            }
        }

        //Intensidad de la fiebre
        ADPp4_2.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (toggleButton.btADPP4_2MuySevera.isPressed) {
                    DataDiagnostico.peligroIntensidadFiebre = 4
                } else if (toggleButton.btADPP4_2Severa.isPressed) {
                    DataDiagnostico.peligroIntensidadFiebre = 3
                } else if (toggleButton.btADPP4_2Moderada.isPressed) {
                    DataDiagnostico.peligroIntensidadFiebre = 2
                } else if (toggleButton.btADPP4_2Leve.isPressed) {
                    DataDiagnostico.peligroIntensidadFiebre = 1
                }
            }
        }

        //dolor de pecho
        ADPp5.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                DataDiagnostico.peligroDolorDePecho = if (toggleButton.btADPP5Si.isPressed) 1 else 0
            }
        }

        btADPAtras4.setOnClickListener() {
            onBackPressed()
        }

        btADPFinalizar4.setOnClickListener() {
            if (!validarRespuestas()) {
                //No paso la validacion
                Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //Toast.makeText(this, "pasa a la siguiente activity", Toast.LENGTH_SHORT).show()
                //se abre la segunda activity de evaluacion de problemas mas serios de salud
                //val intent = Intent(this, AutodiagnosticoPeligrosP2Activity::class.java)
                //startActivity(intent)
                //val diagnostico = generarDiagnosticoPersonaNueva()
                Log.d("Cuidarnos", "Pasar a entregar resultado de paciente con covid")

            }
        }
    }


    fun validarRespuestas():Boolean {
        var exito = false
        layoutADP2.background = resources.getDrawable(R.drawable.border)
        layoutADP3.background = resources.getDrawable(R.drawable.border)
        layoutADP4_1.background = resources.getDrawable(R.drawable.border)
        layoutADP4_2.background = resources.getDrawable(R.drawable.border)
        layoutADP5.background = resources.getDrawable(R.drawable.border)
        if (DataDiagnostico.peligroCansancioFatiga == null) {
            layoutADP2.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP2.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.peligroMalestarGeneral == null) {
            layoutADP3.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP3.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.peligroTieneOTuvoFiebre == null) {
            layoutADP4_1.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP4_1.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.peligroTieneOTuvoFiebre==1 && DataDiagnostico.peligroIntensidadFiebre == null) {
            layoutADP4_2.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP4_2.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.peligroDolorDePecho == null) {
            layoutADP5.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutADP5.background = resources.getDrawable(R.drawable.border_red)
        } else {
            exito = true
        }
        return exito
    }
}