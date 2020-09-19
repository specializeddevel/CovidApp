package org.csrabolivia.covidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AccesoAplicacionActivity : AppCompatActivity() {

    var nombre: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceso_aplicacion)

        //nombre= intent.getStringExtra("NOMBRE", 0)
    }
}