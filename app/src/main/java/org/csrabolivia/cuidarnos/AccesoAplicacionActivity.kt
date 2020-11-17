package org.csrabolivia.cuidarnos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_acceso_aplicacion.*
import org.csrabolivia.cuidarnos.R
import org.csrabolivia.cuidarnos.jsondata.Variables
import kotlin.system.exitProcess


class AccesoAplicacionActivity : AppCompatActivity() {

    var nombre: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceso_aplicacion)

        btIniciarAutoevaluacion.setOnClickListener(){
            Log.i("Cuidarnos", "Se inicia autoevaluación")
            val intent = Intent(this, AutodiagnosticoInicialActivity::class.java)
            startActivity(intent)
        }

        btEditarDatos.setOnClickListener(){
            Log.i("Cuidarnos", "Se inicia edicion de datos personales")
            val intent = Intent(this, PageOneActivity::class.java)
            startActivity(intent)
        }

        if(Variables.primeraVez){
            editarDatosCardView.visibility = View.GONE
        }

        btInfoCovid.setOnClickListener(){
            Log.d("Cuidarnos", "Informacion COVID")

        }

        if(!Variables._conDatosGPS && !Variables.primeraVez){
            Log.d("Cuidarnos", Variables._conDatosGPS.toString())
            val intent = Intent(this, GeoreferenciaActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        /*moveTaskToBack(true)
        exitProcess(-1)
        finishAffinity()*/
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcess(0)
    }
}