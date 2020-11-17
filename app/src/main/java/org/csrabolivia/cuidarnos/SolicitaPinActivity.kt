package org.csrabolivia.cuidarnos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import com.google.errorprone.annotations.Var
import kotlinx.android.synthetic.main.activity_page_one.*
import kotlinx.android.synthetic.main.activity_solicita_pin.*
import org.csrabolivia.cuidarnos.jsondata.Variables
import org.csrabolivia.cuidarnos.utiles.InputFilterMinMax
import kotlin.system.exitProcess

class SolicitaPinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicita_pin)

        var numeroPIN: Int

        textFieldPIN.editText?.setFilters(arrayOf<InputFilter>(InputFilterMinMax(0,9999)))

        btEntrarPin.setOnClickListener(){
            numeroPIN = if (textFieldPIN.editText?.text.toString().isEmpty()){
                0
            } else {
                (textFieldPIN.editText?.text.toString().toInt())
            }
            if (validaPin(numeroPIN)) {
                //pasa el pin
                Variables.PIN = numeroPIN
                val intent = Intent(this, PageOneActivity::class.java)
                intent.putExtra("ID", Variables.IDUNICO)
                startActivity(intent)
            } else {
                //no pasa el pin
                //Toast.makeText(this, "El PIN introducido no es válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validaPin(pin : Int): Boolean {
        textFieldPIN.error = null
        var retorno = false
        if (textFieldPIN.editText?.text.toString().trim().isEmpty()) {
            textFieldPIN.error = "Valor requerido"
            textFieldPIN.requestFocus()
        } else {
            when (pin) {
                1015, 1714, 2406, 2437, 2693, 2697, 2977, 3159, 3565, 3813, 3858, 4228, 4267, 4328, 4792,
                4889, 5018, 6338, 6416, 6684, 7081, 7640, 8137, 8184, 8246, 8729, 8757, 9345, 9428, 9673 -> retorno =
                    true
                else -> {
                    textFieldPIN.error = "PIN no reconocido!!!"
                }
            }
        }
        return  retorno
    }

    override fun onBackPressed() {
        if (Variables.primeraVez) {
            super.onBackPressed()
            finishAffinity()
        } else {
            val intent = Intent(this, AccesoAplicacionActivity::class.java)
            startActivity(intent)
        }

    }

}
