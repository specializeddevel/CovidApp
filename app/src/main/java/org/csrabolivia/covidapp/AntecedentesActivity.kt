package org.csrabolivia.covidapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_antecedentes.*
import kotlinx.android.synthetic.main.activity_antecedentes.view.*
import kotlinx.android.synthetic.main.activity_page_two.*
import kotlinx.android.synthetic.main.activity_page_two.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AntecedentesActivity : AppCompatActivity() {

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

        //Escuclar boton Continuar
        btContinuar4.setOnClickListener(){
            if(!validarRespuestas()){
                Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT)
                    .show()
            }
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
}


