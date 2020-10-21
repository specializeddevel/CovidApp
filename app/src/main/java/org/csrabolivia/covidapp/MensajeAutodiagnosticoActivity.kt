package org.csrabolivia.covidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_mensaje_autodiagnostico.*

private val key = "PERSONALDATA"

class MensajeAutodiagnosticoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensaje_autodiagnostico)

        //Niveles de riesgo
        //0 -> sin ningun sintoma y sin contacto covid
        //1 -> sin ningun sintoma y con contacto covid
        //2 -> con sintomas leves
        //3 -> con sintomas moderados
        //4 -> con sintomas graves
        val nombreCompleto = "${Variables.NOMBRES}  ${Variables.APELLIDOS}"
        mensaje2Layout.visibility = View.GONE
        mensaje3Layout.visibility = View.GONE
        mensaje3Layout.visibility = View.GONE
        mensaje4Layout.visibility = View.GONE
        mensaje5Layout.visibility = View.GONE
        mensaje6Layout.visibility = View.GONE

        when(DataDiagnostico.nivelDeRieso){
            0 -> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_green)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Sin embargo, es recomendable que haga el autodiagnostico nuevamente cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor recuerde seguir las siguientes recomendaciones:")
                //mensaje 2
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje2Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca, siempre que se encuentre fuera de su vivienda.")
                //mensaje 3
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__01_long_distance)
                mensaje3Texto.setText("Mantenga la distancia con otras personas, separándose de ellas por al menos 1 metro de distancia.")
                //mensaje 4
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_handwash)
                mensaje4Texto.setText("Lávese las manos con agua y jabón, de manera frecuente y por lo menos por 20 segundos.")
            }
            1 -> {
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19, pero tiene riesgo")
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Sin embargo a causa de que estuvo en contacto con una persona con COVID-19, es recomendable que haga el autodiagnostico nuevamente cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor recuerde seguir las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger)
                //mensaje2Imagen.
                mensaje2Texto.setText("En caso de que presente cualquier tipo de sintoma, debera realizar el autodiagnostico para evitar llegar a una situción de gravedad.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca, siempre que se encuentre fuera de su vivienda.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic__01_long_distance)
                mensaje4Texto.setText("Mantenga la distancia con otras personas, separándose de ellas por al menos 1 metro de distancia.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_handwash)
                mensaje5Texto.setText("Lávese las manos con agua y jabón, de manera frecuente y por lo menos por 20 segundos.")
            }
            else -> Toast.makeText(this, "Valor de nivel de riesgo no reconocido", Toast.LENGTH_SHORT).show()
        }

        btFinalizar1.setOnClickListener(){

        }




    }
}