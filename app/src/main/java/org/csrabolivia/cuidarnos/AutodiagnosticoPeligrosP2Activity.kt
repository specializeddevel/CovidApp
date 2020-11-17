 package org.csrabolivia.cuidarnos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p2.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p2.view.*
import org.csrabolivia.cuidarnos.R
import org.csrabolivia.cuidarnos.jsondata.DataDiagnostico
import org.csrabolivia.cuidarnos.jsondata.Variables
import www.sanju.motiontoast.MotionToast
import java.util.*

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
                 DataDiagnostico.peligroDolorDePecho =
                     if (toggleButton.btADPP5Si.isPressed) 1 else 0
             }
         }

         btADPAtras4.setOnClickListener() {
             onBackPressed()
         }

         btADPFinalizar4.setOnClickListener() {
             if (!validarRespuestas()) {
                 //No paso la validacion
                 MotionToast.createColorToast(
                     this, "Error!",
                     "Por favor responda todas las casillas!",
                     MotionToast.TOAST_ERROR,
                     MotionToast.GRAVITY_BOTTOM,
                     MotionToast.LONG_DURATION,
                     ResourcesCompat.getFont(this, R.font.helvetica_regular)
                 )
             } else {
                 DataDiagnostico.nivelDeRieso = generarDiagnosticoPersonaCovid()
                 Log.d("Cuidarnos", "Nivel de riesgo calculado: ${DataDiagnostico.nivelDeRieso}")
                 val intent = Intent(this, MensajeAutodiagnosticoActivity::class.java)
                 /*if (DataDiagnostico.tieneContactoCovid != 1)
                     DataDiagnostico.nivelDeRieso = 0 //No tiene molestias ni tuvo contacto con una persona con o sospechosa de covid
                 else {
                     DataDiagnostico.nivelDeRieso = 1 //No tiene molestias pero tuvo contacto con una persona con o sospechosa de covid
                 }*/
                 startActivity(intent)
                 Log.d("Cuidarnos", "Pasar a entregar resultado de paciente con covid")

             }
         }
     }


     fun generarDiagnosticoPersonaCovid(): Int {
         var retorno = 99  //Se retorna 99 si es una persona que no tieee covid, no ceberia haber entrado nunca aqui
         val sumaSintomas =
             DataDiagnostico.peligroDificuldadRespiratoria!! + DataDiagnostico.peligroCansancioFatiga!! + DataDiagnostico.peligroMalestarGeneral!! +
                     DataDiagnostico.peligroTieneOTuvoFiebre!! + DataDiagnostico.peligroDolorDePecho!!
         Variables.EDAD_ANOS = calcularEdadAnos(Variables.FNACIMIENTO)
         DataDiagnostico.peligroAdultoMayor = if (Variables.EDAD_ANOS!! >= 60) 1 else 0
         Variables.varEmbarazada =
             if (Variables.varEmbarazada == null) 0 else Variables.varEmbarazada
         val sumaFactores =
             DataDiagnostico.peligroAdultoMayor!! + Variables.varAntecedente1!! + Variables.varAntecedente2!! + Variables.varAntecedente3!! +
                     Variables.varAntecedente4!! + Variables.varAntecedente5!! + Variables.varAntecedente6!! + Variables.varAntecedente7!! + Variables.varAntecedente8!! +
                     Variables.varEmbarazada!!
         Log.d("Cuidarnos", "Edad en años: ${Variables.EDAD_ANOS} ${sumaFactores}")
         if (DataDiagnostico.tieneCovid == 1) {
             if (sumaSintomas == 0 && sumaFactores == 0) {
                 //Paciente sin ninguna complicacion
                 retorno = 30
             } else if (sumaSintomas >= 1 && sumaFactores == 0) {
                 //Paciente con sintoimas pero sin antecedentes
                 retorno = 31
             }else if (sumaSintomas == 0 && sumaFactores >= 1) {
                     //Paciente sin sintoimas pero con antecedentes
                     retorno = 31
             } else if (sumaSintomas >= 1 && sumaFactores >= 1) {
                 //Paciente con sintomas y con antecedentes
                 retorno = 32
             }
         }
         return retorno
     }


     fun calcularEdadAnos(fechaNac: String ): Int {

         val DOD: Int = fechaNac.substring(0,2).toInt()
         val DOM: Int = fechaNac.substring(3,5).toInt()
         val DOY: Int = fechaNac.substring(6,10).toInt()

         var diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
         val mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1
         val anoActual = Calendar.getInstance().get(Calendar.YEAR)


         var month: Int? = null
         var yea: Int? = null
         var day: Int? = null

         if (DOM == mesActual) {
             yea = (anoActual - DOY) - 1
             month = mesActual - DOM + 11
             diaActual++
         } else if (mesActual >= DOM) {
             month = (DOM - mesActual)
             yea = anoActual - DOY
             diaActual++
         } else {
             month = (12 + mesActual) - DOM
             yea = (anoActual - DOY) - 1
         }
         if (DOD > diaActual) {
             day = diaActual - DOD
             day = 30 + day
         } else {
             day = diaActual - DOD
         }
         day = Math.abs(day)
         month = Math.abs(month)
         return yea
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