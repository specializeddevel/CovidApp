package org.csrabolivia.covidapp

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.likethesalad.android.aaper.api.EnsurePermissions
import kotlinx.android.synthetic.main.activity_mensaje_autodiagnostico.*
import org.csrabolivia.covidapp.jsondata.DataDiagnostico
import org.csrabolivia.covidapp.jsondata.Variables
import java.io.IOException
import java.util.*

private val key = "PERSONALDATA"

class MensajeAutodiagnosticoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensaje_autodiagnostico)

        //Niveles de riesgo
        //0 -> Sin ningún síntoma y sin contacto con paciente covid y sin factor de riesgo
        //1 -> Sin ningún síntoma y con contacto con paciente covid sin factor de riesgo
        //2 -> Sin ningún síntoma y con contacto con paciente covid y con al menos un factor de riesgo
        //3 -> Sin ningún síntoma y sin contacto con paciente covid y con al menos un factor de riesgo
        //10 -> Con al menos dos sintomas, al menos un factor de riesgo y contacto covid
        //11 -> Con al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
        //12 -> Con al menos dos sintomas, sin factor de riesgo y con contacto covid
        //13 -> Con al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y con contacto covid
        //14 -> Con al menos dos sintomas, al menos un factor de riesgo y sin contacto covid
        //15 -> Con al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
        //16 -> Con al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
        //17 -> Con al menos dos sintomas (no incluido perdida de olfato o gusto), sin factor de riesgo y sin contacto covid
        //20 -> Con un sintoma, al menos un factor de riesgo y contacto covid
        //21 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
        //22 -> Con un sintoma, sin factor de riesgo y con contacto covid
        //23 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y con contacto covid
        //24 -> Con un sintoma, al menos un factor de riesgo y sin contacto covid
        //25 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
        //26 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
        //27 -> Con un sintoma (no incluido perdida de olfato o gusto), sin factor de riesgo y sin contacto covid


        val nombreCompleto = "${Variables.NOMBRES}  ${Variables.APELLIDOS}"
        mensaje2Layout.visibility = View.GONE
        mensaje3Layout.visibility = View.GONE
        mensaje4Layout.visibility = View.GONE
        mensaje5Layout.visibility = View.GONE
        mensaje6Layout.visibility = View.GONE
        llamadaLayout.visibility = View.GONE

        btResultadoLlamarMedico.setOnClickListener(){
            hacerLlamada()
        }

        when(DataDiagnostico.nivelDeRieso){
            //Sin ningún síntoma y sin contacto con paciente covid y sin factor de riesgo
            0 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_low)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_green)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_comprobar)
                ////headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Le recomendamos que realice una nueva autoevaluación dentro de:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("3")
                diagnosticoHoraTexto.setText("DÍAS")
                //mensaje1Texto.setText("Por favor recuerde seguir las siguientes recomendaciones:")
                //mensaje 2
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje2Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                //mensaje 3
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_distanciamiento_social)
                mensaje3Texto.setText(R.string.autoevaluacion_distanciamiento)
                //mensaje 4
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje4Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //Sin ningún síntoma y con contacto con paciente covid sin factor de riesgo
            1 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                ////headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19, pero existe probabilidad de tenerlo")
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Su evaluación no es compatible con COVID-19, pero existe sospecha leve de contagio porque recientemente estuvo en contacto con una persona con COVID-19. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                ////mensaje1Texto.setText("Por favor recuerde seguir las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_si)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_nuevo_sintoma)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                mensaje3Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 2
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje4Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                //mensaje 3
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_distanciamiento_social)
                mensaje5Texto.setText(R.string.autoevaluacion_distanciamiento)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje6Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //Sin ningún síntoma y con contacto con paciente covid y con al menos un factor de riesgo
            2 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                ////headerSubtitulo.setText("No presenta ningún sintoma de COVID-19, pero existe probabilidad de tenerlo.")
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Su evaluación no es compatible con COVID-19, pero existe sospecha leve de contagio porque recientemente estuvo en contacto con una persona con COVID-19. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor recuerde seguir cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_si)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_nuevo_sintoma)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_distanciamiento_social)
                mensaje4Texto.setText(R.string.autoevaluacion_distanciamiento)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje5Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //Sin ningún síntoma y sin contacto con paciente covid y con al menos un factor de riesgo
            3 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_low)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_green)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_comprobar)
                ////headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19 pero presenta factores de riesgo")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("3")
                diagnosticoHoraTexto.setText("DIAS")
                //mensaje1Texto.setText("Por favor recuerde seguir cuidadosamente las siguientes recomendaciones:")
                //mensaje 2
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje2Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                //mensaje 3
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_distanciamiento_social)
                mensaje3Texto.setText(R.string.autoevaluacion_distanciamiento)
                //mensaje 4
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje4Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //10 - Con al menos dos sintomas, al menos un factor de riesgo y contacto covid
            10 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y alto riesgo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 y existe la probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_telefono_inteligente)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                llamadaLayout.visibility = View.VISIBLE
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //11 -> Con al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
            11 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas altamente compatibles con COVID-19 y alto riesgo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas altamente compatibles con COVID-19 y elevada probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19 y presenta síntomas muy específicos. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_telefono_inteligente)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //12 -> Con al menos dos sintomas, sin factor de riesgo y con contacto covid
            12 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo médio.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 y existe la probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-1. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por su seguridad y la de su familia, por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //13 -> Con al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y con contacto covid
            13 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas altamente compatibles con COVID-19 y probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas altamente compatibles con COVID-19 y elevada probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es muy compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19 y presenta síntomas muy específicos. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_telefono_inteligente)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //14 - Con al menos dos sintomas, al menos un factor de riesgo y sin contacto covid
            14 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo moderado.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 pero baja probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_alto)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_aislamiento_14)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //15 -> Con al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
            15 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas altamente compatibles con COVID-19 y probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas altamente compatibles con COVID-19 y elevada probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque presenta síntomas muy específicos. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //16 -> Con al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
            16 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque presenta síntomas muy específicos. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_low)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano_low)
            }
            //17 -> Con al menos dos sintomas (no incluido perdida de olfato o gusto), sin factor de riesgo y sin contacto covid
            17 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 pero baja probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es levemente compatible con COVID-19. Por favor realice otra autoevaluación en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_low)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano_low)
            }
            //20 -> Con un sintoma, al menos un factor de riesgo y contacto covid
            20 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta un síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 y probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_low)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano_low)
            }
            //21 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
            21 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma altamente compatible con COVID-19 y alto riesgo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma altamente compatible con COVID-19 y existe la probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19 y presenta un síntoma muy específico. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_alto)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //22 -> Con un sintoma, sin factor de riesgo y con contacto covid
            22 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 y existe la probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por su seguridad y la de su familia, por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_alto)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
            }
            //23 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y con contacto covid
            23 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma altamente compatible con COVID-19 y probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma altamente compatible con COVID-19 y elevada probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque recientemente estuvo en contacto con una persona con COVID-19 y presenta un síntoma muy específico. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_alto)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_familia_aislada)
            }
            //24 -> Con un sintoma, al menos un factor de riesgo y sin contacto covid
            24 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD BAJA DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo moderado.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 pero baja probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es levemente compatible con COVID-19. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_si)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_nuevo_sintoma)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_distanciamiento_social)
                mensaje4Texto.setText(R.string.autoevaluacion_distanciamiento)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje5Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //25 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
            25 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y mediana probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma altamente compatible con COVID-19 y existe la probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es compatible con COVID-19 y la sospecha es mayor porque presenta un síntoma muy específico. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_bajo)
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_alto)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_low)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano_low)
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText(R.string.autoevaluacion_aislamiento_familia)
            }
            //26 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
            26 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD BAJA DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 pero baja probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es levemente compatible con COVID-19. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_si)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_nuevo_sintoma)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_low)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText(R.string.autoevaluacion_no_use_mismo_bano_low)
            }
            //27 -> Con un sintoma (no incluido perdida de olfato o gusto), sin factor de riesgo y sin contacto covid
            27 -> {
                //inicializa los peligros de un test previo a null
                inicializaPeligros()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD BAJA DE COVID-19")
                ////headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 pero baja probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su evaluación es levemente compatible con COVID-19. Por favor realice otra autoevaluación cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("3")
                diagnosticoHoraTexto.setText("DÍAS")
                //mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_si)
                //mensaje2Imagen.
                mensaje2Texto.setText(R.string.autoevaluacion_nuevo_sintoma)
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje3Texto.setText(R.string.autoevaluacion_use_barbijo_bajo)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_distanciamiento_social)
                mensaje4Texto.setText(R.string.autoevaluacion_distanciamiento)
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje5Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //Paciente COVID sin complicaciones
            30 -> {
                //Borra los sintomas registrados en un test previo
                inicializaSintomas()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_low)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_green)
                headerTitulo.setText("NO PRESENTA COMPLICACIONES")
                headerImagen.setImageResource(R.drawable.ic_comprobar)
                ////headerSubtitulo.setText("No presenta ninguna complicación ni riesgo")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>de acuerdo a los datos otorgados no presenta complicaciónes en su estado de salud. Esta llevando la enfermedad </b>"))
                diagnosticoLinea2.setText("Realice una nueva autoevaluación dentro de:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("RECUERDE:")
                //mensaje 2
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_mascara)
                mensaje2Texto.setText(R.string.autoevaluacion_use_barbijo_alto)
                //mensaje 3
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje3Texto.setText(R.string.autoevaluacion_aislece_con_covid)
                //mensaje 4
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje4Texto.setText(R.string.autoevaluacion_no_use_mismo_bano)
                //mensaje 5
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_platos)
                mensaje5Texto.setText(R.string.autoevaluacion_utencilios_covid)
                //mensaje 6
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_lavarse_las_manos)
                mensaje6Texto.setText(R.string.autoevaluacion_lavese_las_manos)
            }
            //Paciente COVID con complicaciones
            31 -> {
                //Borra los sintomas registrados en un test previo
                inicializaSintomas()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_high)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerTitulo.setText("PRESENTA COMPLICACIONES")
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                ////headerSubtitulo.setText("Presenta complicaciones")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>de acuerdo a los datos otorgados, presenta algunas complicaciónes en su estado de salud actual.<br><br>Presione el botón [LLAMAR A UN MÉDICO] para ser atendido por personal de salud calificado.</b>"))
                diagnosticoLinea2.setText("Para informar nuevamente de su estado al personal de salud, realice una nueva autoevaluación dentro de:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("RECUERDE:")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje 2
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 3
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_con_covid_alto)
            }
            //Paciente COVID con complicaciones
            32 -> {
                //Borra los sintomas registrados en un test previo
                inicializaSintomas()
                tableLayoutTarjetaDiagnostico.setBackgroundResource(R.drawable.background_shape_exposure_infected)
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerTitulo.setText("PRESENTA COMPLICACIONES Y RIESGO")
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                ////headerSubtitulo.setText("Presenta complicaciones")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>de acuerdo a los datos otorgados, presenta complicaciones en su estado de salud que deben ser evaluadas inmediatamente.<br><br>Presione el botón [LLAMAR A UN MÉDICO] para ser atendido por personal de salud calificado.</b>"))
                diagnosticoLinea2.setText("Para informar nuevamente de su estado al personal de salud, realice una nueva autoevaluación dentro de:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                //mensaje1Texto.setText("RECUERDE:")
                llamadaLayout.visibility = View.VISIBLE
                //mensaje 2
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_enviar_datos)
                mensaje2Texto.setText(R.string.autoevaluacion_envio_datos_alto)
                //mensaje 3
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic_llamada_telefonica)
                mensaje3Texto.setText(R.string.autoevaluacion_llame_personal_salud)
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText(R.string.autoevaluacion_aislece_con_covid_alto)
            }
            else -> Toast.makeText(
                this,
                "Valor de nivel de riesgo no reconocido",
                Toast.LENGTH_SHORT
            ).show()
        }

        btFinalizarDiagnostico.setOnClickListener() {
            if (verificaInternet()) {
                val diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1
                val anoActual = Calendar.getInstance().get(Calendar.YEAR)
                val horaActual = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val minutoActual = Calendar.getInstance().get(Calendar.MINUTE)
                val diaActualCadena = if (diaActual <= 9) "0${diaActual}" else diaActual
                val mesActualCadena = if (mesActual <= 9) "0${mesActual}" else mesActual
                val horaActualCadena = if (horaActual <= 9) "0${horaActual}" else horaActual
                val minutoActualCadena = if (minutoActual <= 9) "0${minutoActual}" else minutoActual
                val segundoActualCadena =
                    if (minutoActual <= 9) "0${minutoActual}" else minutoActual
                val fechaActualCompleta = "$diaActualCadena-$mesActualCadena-$anoActual"
                val horaActualCompleta =
                    "$horaActualCadena:$minutoActualCadena:$segundoActualCadena"
                val db = FirebaseFirestore.getInstance()
                if (!Variables.IDUNICO.equals("desconocido")) {
                    /*db.collection("usuarios").document(Variables.IDUNICO).collection("diagnosticos")
                        .add(
                            hashMapOf
                                (
                                "id_unica" to Variables.IDUNICO,
                                "fecha_actual" to fechaActualCompleta,
                                "hora_actual" to horaActualCompleta,
                                "tiene_covid" to DataDiagnostico.tieneCovid,
                                "tiene_contacto_covid" to DataDiagnostico.tieneContactoCovid,
                                "tos_seca" to DataDiagnostico.tosSeca,
                                "fiebre" to DataDiagnostico.fiebre,
                                "malestar" to DataDiagnostico.malestar,
                                "dolor_cabeza" to DataDiagnostico.dolorCabeza,
                                "dificultad_respiratoria" to DataDiagnostico.dificultadRespirar,
                                "dolor_muscular" to DataDiagnostico.dolorMuscular,
                                "dolor_garganta" to DataDiagnostico.dolorGarganta,
                                "perdida_olfato" to DataDiagnostico.perdidaOlfato,
                                "perdida_gusto" to DataDiagnostico.perdidaGusto,
                                "peligro_adulto_mayor" to DataDiagnostico.peligroAdultoMayor,
                                "peligro_dificultad_respiratoria" to DataDiagnostico.peligroDificuldadRespiratoria,
                                "peligro_tiempo_dificultad_respiratoria" to DataDiagnostico.peligroTiempoDificultadRespiratoria,
                                "peligro_severidad_dificultad_respiratoria" to DataDiagnostico.peligroSeveridadDificultadRespiratoria,
                                "peligro_cansancio_fatiga" to DataDiagnostico.peligroCansancioFatiga,
                                "peligro_malestar_general" to DataDiagnostico.peligroMalestarGeneral,
                                "peligro_fiebre" to DataDiagnostico.peligroTieneOTuvoFiebre,
                                "peligro_intensidad_fiebre" to DataDiagnostico.peligroIntensidadFiebre,
                                "peligro_dolor_pecho" to DataDiagnostico.peligroDolorDePecho,
                                "nivel_riesgo" to DataDiagnostico.nivelDeRieso,
                            ),
                        )*/
                    //db.collection("usuarios").document(Variables.IDUNICO).collection("diagnosticos").document("autodiagnostico").set(
                    db.collection("usuarios").document(Variables.IDUNICO).set(
                        hashMapOf
                            (
                            "autoevaluacion_fecha_actual" to fechaActualCompleta,
                            "autoevaluacion_hora_actual" to horaActualCompleta,
                            "autoevaluacion_tiene_covid" to DataDiagnostico.tieneCovid,
                            "autoevaluacion_tiene_contacto_covid" to DataDiagnostico.tieneContactoCovid,
                            "autoevaluacion_tos_seca" to DataDiagnostico.tosSeca,
                            "autoevaluacion_fiebre" to DataDiagnostico.fiebre,
                            "autoevaluacion_malestar" to DataDiagnostico.malestar,
                            "autoevaluacion_dolor_cabeza" to DataDiagnostico.dolorCabeza,
                            "autoevaluacion_dificultad_respiratoria" to DataDiagnostico.dificultadRespirar,
                            "autoevaluacion_dolor_muscular" to DataDiagnostico.dolorMuscular,
                            "autoevaluacion_dolor_garganta" to DataDiagnostico.dolorGarganta,
                            "autoevaluacion_perdida_olfato" to DataDiagnostico.perdidaOlfato,
                            "autoevaluacion_perdida_gusto" to DataDiagnostico.perdidaGusto,
                            "autoevaluacion_peligro_adulto_mayor" to DataDiagnostico.peligroAdultoMayor,
                            "autoevaluacion_peligro_dificultad_respiratoria" to DataDiagnostico.peligroDificuldadRespiratoria,
                            "autoevaluacion_peligro_tiempo_dificultad_respiratoria" to DataDiagnostico.peligroTiempoDificultadRespiratoria,
                            "autoevaluacion_peligro_severidad_dificultad_respiratoria" to DataDiagnostico.peligroSeveridadDificultadRespiratoria,
                            "autoevaluacion_peligro_cansancio_fatiga" to DataDiagnostico.peligroCansancioFatiga,
                            "autoevaluacion_peligro_malestar_general" to DataDiagnostico.peligroMalestarGeneral,
                            "autoevaluacion_peligro_fiebre" to DataDiagnostico.peligroTieneOTuvoFiebre,
                            "autoevaluacion_peligro_intensidad_fiebre" to DataDiagnostico.peligroIntensidadFiebre,
                            "autoevaluacion_peligro_dolor_pecho" to DataDiagnostico.peligroDolorDePecho,
                            "autoevaluacion_nivel_riesgo" to DataDiagnostico.nivelDeRieso
                        ), SetOptions.merge()
                    )
                    Log.i(
                        "Cuidarnos",
                        "Se registraron los datos del diagnostico en la nube y retrorna al menu principal"
                    )
                    val intent = Intent(this, AccesoAplicacionActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.i(
                        "Cuidarnos",
                        "No se pueden registrar los datos por que no se tiene ID unica"
                    )
                }
            }
            else {
                Log.i(
                    "Cuidarnos",
                    "No se pueden registrar los datos en la nuve por que no se tiene conexion a internet"
                )
                Toast.makeText(
                    this,
                    "Por favor conectese a Internet para enviar el diagnóstico.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun inicializaSintomas(){
        DataDiagnostico.tieneContactoCovid = null
        DataDiagnostico.tosSeca = null
        DataDiagnostico.fiebre = null
        DataDiagnostico.dolorCabeza = null
        DataDiagnostico.dificultadRespirar = null
        DataDiagnostico.dolorMuscular = null
        DataDiagnostico.dolorGarganta = null
        DataDiagnostico.perdidaGusto = null
        DataDiagnostico.perdidaOlfato = null
    }

    fun inicializaPeligros(){
        DataDiagnostico.peligroDificuldadRespiratoria = null
        DataDiagnostico.peligroSeveridadDificultadRespiratoria = null
        DataDiagnostico.peligroTiempoDificultadRespiratoria = null
        DataDiagnostico.peligroCansancioFatiga = null
        DataDiagnostico.peligroMalestarGeneral = null
        DataDiagnostico.peligroTieneOTuvoFiebre = null
        DataDiagnostico.peligroIntensidadFiebre = null
        DataDiagnostico.peligroDolorDePecho = null
        //DataDiagnostico.peligroAdultoMayor = null
    }

    @EnsurePermissions(permissions = [Manifest.permission.CALL_PHONE])
    fun hacerLlamada(){
        Log.d("Cuidarnos", "intentando hacer llamada")
        val intent = Intent(Intent.ACTION_CALL, Uri.parse(Variables.numeroTelefonoMedico))
        startActivity(intent)
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