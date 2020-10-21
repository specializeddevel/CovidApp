package org.csrabolivia.covidapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.errorprone.annotations.Var
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p2.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_peligros_p2.view.*
import java.lang.Math.abs
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
                val diagnostico = generarDiagnostico()
                Log.d("Cuidarnos", "Diagnostico: $diagnostico")

            }
        }
    }

    fun generarDiagnostico(): Int {
        var retorno = 99  //Si se retorna 99 Es paciente con COVID, no deberia haber entrado a esta funcion
        val sumaSintomas =
            DataDiagnostico.tosSeca!! + DataDiagnostico.fiebre!! + DataDiagnostico.malestar!! +
                    DataDiagnostico.dolorCabeza!! + DataDiagnostico.dificultadRespirar!! + DataDiagnostico.dolorMuscular!! +
                    DataDiagnostico.dolorGarganta!! + DataDiagnostico.perdidaOlfato!! + DataDiagnostico.perdidaGusto!!
        Variables.EDAD_ANOS = calcularEdadAnos(Variables.FNACIMIENTO)
        DataDiagnostico.peligroAdultoMayor = if (Variables.EDAD_ANOS!! >= 60) 1 else 0
        Variables.varEmbarazada = if (Variables.varEmbarazada == null) 0 else Variables.varEmbarazada
        val sumaFactores = DataDiagnostico.peligroAdultoMayor!! + Variables.varAntecedente1!! + Variables.varAntecedente2!! + Variables.varAntecedente3!! +
                Variables.varAntecedente4!! + Variables.varAntecedente5!! + Variables.varAntecedente6!! + Variables.varAntecedente7!! + Variables.varAntecedente8!! +
                Variables.varEmbarazada!!
        Log.d("Cuidarnos", "Edad en años: ${Variables.EDAD_ANOS} ${sumaFactores}")
        if (DataDiagnostico.tieneCovid==0) {
            if (sumaSintomas >= 2) {
                if (DataDiagnostico.tieneContactoCovid == 1) {
                    if (sumaFactores >= 1) {
                        //Con al menos dos sintomas, al menos un factor de riesgo y contacto covid
                        retorno = 10
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
                            retorno = 11
                        }
                    } else {
                        //al menos dos sintomas, sin factor de riesgo y con contacto covid
                        retorno = 12
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y con contacto covid
                            retorno = 13
                        }
                    }
                } else {
                    if (sumaFactores >= 1) {
                        //al menos dos sintomas, al menos un factor de riesgo y sin contacto covid
                        retorno = 14
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
                            retorno = 15
                        }
                    } else {
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
                            retorno = 16
                        }
                    }
                }
            } else {
                //un solo sintoma
                if (DataDiagnostico.tieneContactoCovid == 1) {
                    if (sumaFactores >= 1) {
                        //Con un sintoma, al menos un factor de riesgo y contacto covid
                        retorno = 20
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
                            retorno = 21
                        }
                    } else {
                        //Con un sintoma, sin factor de riesgo y con contacto covid
                        retorno = 22
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y con contacto covid
                            retorno = 23
                        }
                    }
                } else {
                    if (sumaFactores >= 1) {
                        //Con un sintoma, al menos un factor de riesgo y sin contacto covid
                        retorno = 24
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
                            retorno = 25
                        }
                    } else {
                        if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
                            retorno = 26
                        }
                    }
                }
            }
        }
        return retorno
    }

    fun calcularEdadAnos(fechaNac: String ): Int {

        // TODO: 20/10/2020 Se debe verificar que las fechas esten en rangos permitidos en el date picker
        
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
        day = abs(day)
        month = abs(month)
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