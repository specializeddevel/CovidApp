package org.csrabolivia.covidapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_autodiagnostico_inicial.*
import kotlinx.android.synthetic.main.activity_autodiagnostico_inicial.view.*
import java.io.IOException

class AutodiagnosticoInicialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autodiagnostico_inicial)
        //Toast.makeText(this, "ID: ${Constants.IDUNICO}", Toast.LENGTH_SHORT).show()
        DataDiagnostico.tieneCovid = null
        DataDiagnostico.tieneContactoCovid = null
        //pregunta 1
        ADp1.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (toggleButton.btADP1No.isPressed) {
                    DataDiagnostico.tieneCovid = 0
                    // Por si se muestra la segunda pregunta relacionada
                    layoutAD2.visibility = View.VISIBLE
                } else {
                    DataDiagnostico.tieneCovid = 1
                    ADp2.clearChecked()
                    DataDiagnostico.tieneContactoCovid = null
                    layoutAD2.visibility = View.INVISIBLE
                }
                Toast.makeText(this, "p1: ${DataDiagnostico.tieneCovid}, p2: ${DataDiagnostico.tieneContactoCovid}", Toast.LENGTH_SHORT).show()
            }
        }

        //pregunta 2
        ADp2.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (toggleButton.btADP2Si.isPressed) {
                    DataDiagnostico.tieneContactoCovid = 1
                } else {
                    DataDiagnostico.tieneContactoCovid = 0
                }
                Toast.makeText(this, "p1: ${DataDiagnostico.tieneCovid}, p2: ${DataDiagnostico.tieneContactoCovid}", Toast.LENGTH_SHORT).show()
            }
        }

        btADContinuar1.setOnClickListener(){
            if (!verificaInternet()) {
                Toast.makeText(this, "Se requiere una conexión activa a Internet.", Toast.LENGTH_SHORT)
                    .show()
            } else if (!validarRespuestas()) {
                    //No paso la validacion
                    Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT)
                        .show()
            } else {
                if (DataDiagnostico.tieneCovid==0 && DataDiagnostico.tieneContactoCovid!=null){
                    //se abre activity de evaluacion de molestias
                    val intent = Intent(this, AutodiagnosticoMolestiasActivity::class.java)
                    startActivity(intent)
                } else
                {
                    //El paciente tiene COVID y se debe saltar a la activity de sintomas peligrosos
                }
            }
        }
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

    fun validarRespuestas():Boolean {
        var exito = false
        layoutAD1.background = resources.getDrawable(R.drawable.border)
        layoutAD2.background = resources.getDrawable(R.drawable.border)
        if (DataDiagnostico.tieneCovid == null) {
            layoutAD1.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAD1.background = resources.getDrawable(R.drawable.border_red)
        } else if (DataDiagnostico.tieneContactoCovid == null && DataDiagnostico.tieneCovid==0) {
            layoutAD2.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAD2.background = resources.getDrawable(R.drawable.border_red)
        } else {
            exito = true
        }
        return exito
    }
}