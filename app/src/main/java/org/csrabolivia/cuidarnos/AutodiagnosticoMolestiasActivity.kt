package org.csrabolivia.cuidarnos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_autodiagnostico_molestias.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_molestias.view.*
import org.csrabolivia.cuidarnos.R
import org.csrabolivia.cuidarnos.jsondata.DataDiagnostico
import org.csrabolivia.cuidarnos.jsondata.Variables
import www.sanju.motiontoast.MotionToast
import java.io.IOException
import java.util.*

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
                MotionToast.createColorToast(
                    this, "Error!",
                    "Por favor responda todas las preguntas!",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.helvetica_regular)
                )
            } else {
                    DataDiagnostico.nivelDeRieso = generarDiagnosticoPersonaNueva()
                    Log.d("Cuidarnos", "Nivel de riesgo calculado: ${DataDiagnostico.nivelDeRieso}")
                    val intent = Intent(this, MensajeAutodiagnosticoActivity::class.java)
                    /*if (DataDiagnostico.tieneContactoCovid != 1)
                        DataDiagnostico.nivelDeRieso = 0 //No tiene molestias ni tuvo contacto con una persona con o sospechosa de covid
                    else {
                        DataDiagnostico.nivelDeRieso = 1 //No tiene molestias pero tuvo contacto con una persona con o sospechosa de covid
                    }*/
                    startActivity(intent)
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

    fun generarDiagnosticoPersonaNueva(): Int {
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
            if (sumaSintomas == 0 && DataDiagnostico.tieneContactoCovid == 1 && sumaFactores == 0) {
                //Sin ningún síntoma y con contacto con paciente covid sin factor de riesgo
                retorno = 1
            } else if (sumaSintomas == 0 && DataDiagnostico.tieneContactoCovid == 1 && sumaFactores >=1 ) {
                //Sin ningún síntoma y con contacto con paciente covid y con al menos un factor de riesgo
                retorno = 2
            } else if (sumaSintomas == 0 && DataDiagnostico.tieneContactoCovid == 0 && sumaFactores == 0  ) {
                //Sin ningún síntoma y sin contacto con paciente covid y sin factor de riesgo
                retorno = 0
            } else if (sumaSintomas == 0 && DataDiagnostico.tieneContactoCovid == 0 && sumaFactores >=1 ) {
                //Sin ningún síntoma y sin contacto con paciente covid y con al menos un factor de riesgo
                retorno = 3
            }
            else if (sumaSintomas >= 2) {
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
                    } else if (DataDiagnostico.perdidaOlfato == 1 || DataDiagnostico.perdidaGusto == 1) {
                            //al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
                            retorno = 16
                        } else {
                            //al menos dos sintomas no incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
                            retorno = 17
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
                        } else {
                            //un solo sintoma no incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
                            retorno = 27
                        }
                    }
                }
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

}