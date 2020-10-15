package org.csrabolivia.covidapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_antecedentes.*
import kotlinx.android.synthetic.main.activity_antecedentes.view.*
import org.json.JSONObject
import java.io.IOException

class AntecedentesActivity : AppCompatActivity() {

    private val keyAntecedentes = "ANTECEDENTESDATA"

    private var varAntecedente1: Int? = null
    private var varAntecedente2: Int? = null
    private var varAntecedente3: Int? = null
    private var varAntecedente4: Int? = null
    private var varAntecedente5: Int? = null
    private var varAntecedente6: Int? = null
    private var varAntecedente7: Int? = null
    private var varAntecedente8: Int? = null
    private var varEmbarazada: Int? = null
    private var femenino = false

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_antecedentes)

        val bundle: Bundle? = intent.extras
        val genero = bundle!!.getString(Constants.GENERO)
        femenino =  genero.equals("Femenino")

        //Muestra o oculta la pregunta de embarazo en funcion al genero
        if(femenino){
            layoutEmbarazo.visibility = View.VISIBLE
        } else {
            layoutEmbarazo.visibility = View.INVISIBLE
        }

        //Escucha antecedente 1
        antecedente1.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente1 = if (toggleButton.btAntecedente1Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 2
        antecedente2.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente2 = if (toggleButton.btAntecedente2Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 3
        antecedente3.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente3 = if (toggleButton.btAntecedente3Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 4
        antecedente4.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente4 = if (toggleButton.btAntecedente4Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 5
        antecedente5.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente5 = if (toggleButton.btAntecedente5Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 6
        antecedente6.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente6 = if (toggleButton.btAntecedente6Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 7
        antecedente7.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente7 = if (toggleButton.btAntecedente7Si.isPressed){1} else{0}
            }
        }
        //Escucha antecedente 8
        antecedente8.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varAntecedente8 = if (toggleButton.btAntecedente8Si.isPressed){1} else{0}
            }
        }
        //Escucha embarazada
        embarazada.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if(isChecked){
                varEmbarazada = if (toggleButton.btEmbarazadaSi.isPressed) {1} else {0}
            }
        }

        //Escuchar boton Continuar
        btContinuar4.setOnClickListener(){
            if(!validarRespuestas()){
                //No paso la validacion
                Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT)
                    .show()
            }else{
                //Paso la validacion se intenta registro de datos localmente
                if(registroLocalAntecedentes()){
                    Log.i("Info", "Se registraron localmente los datos de antecedentes")
                }
            }

        }

        btAtras3.setOnClickListener() {
            onBackPressed()
        }

    }

    fun validarRespuestas():Boolean {
        var exito = false
        layoutAnt1.background = resources.getDrawable(R.drawable.border)
        layoutAnt2.background = resources.getDrawable(R.drawable.border)
        layoutAnt3.background = resources.getDrawable(R.drawable.border)
        layoutAnt4.background = resources.getDrawable(R.drawable.border)
        layoutAnt5.background = resources.getDrawable(R.drawable.border)
        layoutAnt6.background = resources.getDrawable(R.drawable.border)
        layoutAnt7.background = resources.getDrawable(R.drawable.border)
        layoutAnt8.background = resources.getDrawable(R.drawable.border)
        layoutEmbarazo.background = resources.getDrawable(R.drawable.border)
        if (varAntecedente1 == null) {
            layoutAnt1.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt1.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente2 == null) {
            layoutAnt2.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt2.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente3 == null) {
            layoutAnt3.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt3.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente4 == null) {
            layoutAnt4.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt4.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente5 == null) {
            layoutAnt5.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt5.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente6 == null) {
            layoutAnt6.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt6.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente7 == null) {
            layoutAnt7.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt7.background = resources.getDrawable(R.drawable.border_red)
        } else if (varAntecedente8 == null) {
            layoutAnt8.setBackgroundColor(Color.parseColor("#FFCDD2"))
            layoutAnt8.background = resources.getDrawable(R.drawable.border_red)
        } else if (femenino) {
            if(varEmbarazada==null) {
                layoutEmbarazo.setBackgroundColor(Color.parseColor("#FFCDD2"))
                layoutEmbarazo.background = resources.getDrawable(R.drawable.border_red)
            } else {
                exito = true
            }
        } else {
            exito = true
        }
        return exito
    }

    fun registroLocalAntecedentes():Boolean{
        return try {
            if (!verificaInternet()) {
                Toast.makeText(this, "No hay internet, no se puede continuar", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //Guardado de los datos del usaurio localmente mediante shared preferences
                val REGISTRO = JSONObject()
                REGISTRO.put("idUnico", Constants.IDUNICO)
                REGISTRO.put("antecedente1", varAntecedente1)
                REGISTRO.put("antecedente2", varAntecedente2)
                REGISTRO.put("antecedente3", varAntecedente3)
                REGISTRO.put("antecedente4", varAntecedente4)
                REGISTRO.put("antecedente5", varAntecedente5)
                REGISTRO.put("antecedente6", varAntecedente6)
                REGISTRO.put("antecedente7", varAntecedente7)
                REGISTRO.put("antecedente8", varAntecedente8)
                REGISTRO.put("embarazada", varEmbarazada)
                val cadena: String = REGISTRO.toString()
                Log.i("INFO", "Data antecedentes: $cadena")
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = prefs.edit()
                editor.putString(keyAntecedentes, cadena)
                editor.apply()

                val conDatos = prefs.getString(keyAntecedentes, "SD")
                Log.d(
                    "Cuidarnos",
                    "Se guardaron localmente los antecedentes del usuario: ${conDatos.toString()}"
                )
                //Guardar datos en la BD remota en firestore
                if (!Constants.IDUNICO.equals("desconocido")) {
                    db.collection("usuarios").document(Constants.IDUNICO).set(
                        hashMapOf
                            (
                            "diabetes" to varAntecedente1,
                            "obesidad" to varAntecedente2,
                            "dialisis" to varAntecedente3,
                            "presion" to varAntecedente4,
                            "corazon" to varAntecedente5,
                            "pulmones" to varAntecedente6,
                            "cancer" to varAntecedente7,
                            "sida" to varAntecedente8,
                            "embarazada" to varEmbarazada
                        ), SetOptions.merge()
                    )
                    Log.i("Cuidarnos", "Se registraron los antecedentes en la nube")
                    if (Constants.primeraVez) {
                        //Se abrira la activity de autodiagnostico si es la primera vez que se ejecuta la aplicacion
                        intent = Intent(this, AutodiagnosticoInicialActivity::class.java)
                        //intent.putExtra(Constants.GENERO, genero)
                        startActivity(intent)
                    } else {
                        //Se abrira la activity de pantalla principal por que no es la primera vez que se ejecuta la aplicacion y seguramente se estaban editando los datos
                        intent = Intent(this, AccesoAplicacionActivity::class.java)
                        startActivity(intent)
                    }

                } else {
                    Log.i(
                        "Cuidarnos",
                        "Error crítico! No se registraron los datos en la nube por falta de ID"
                    )
                    Toast.makeText(this, "Error crítico al registrar datos de antecedentes", Toast.LENGTH_SHORT).show()
                }

            }
            true
        } catch (e: IOException) {
            Log.i("ERROR", "Se produjo una excepción al registrar los datos de antecedentes localmente: ${e.localizedMessage}")
            false
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
}


