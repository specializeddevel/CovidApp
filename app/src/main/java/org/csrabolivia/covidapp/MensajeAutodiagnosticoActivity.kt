package org.csrabolivia.covidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_mensaje_autodiagnostico.*
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
        //20 -> Con un sintoma, al menos un factor de riesgo y contacto covid
        //21 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
        //22 -> Con un sintoma, sin factor de riesgo y con contacto covid
        //23 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y con contacto covid
        //24 -> Con un sintoma, al menos un factor de riesgo y sin contacto covid
        //25 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
        //26 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y sin contacto covid

        val nombreCompleto = "${Variables.NOMBRES}  ${Variables.APELLIDOS}"
        mensaje2Layout.visibility = View.GONE
        mensaje3Layout.visibility = View.GONE
        mensaje3Layout.visibility = View.GONE
        mensaje4Layout.visibility = View.GONE
        mensaje5Layout.visibility = View.GONE
        mensaje6Layout.visibility = View.GONE

        when(DataDiagnostico.nivelDeRieso){
            //Sin ningún síntoma y sin contacto con paciente covid y sin factor de riesgo
            0 -> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_green)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_checked)
                headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Sin embargo es recomendable que haga el autodiagnostico cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("3")
                diagnosticoHoraTexto.setText("DÍAS")
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
            //Sin ningún síntoma y con contacto con paciente covid sin factor de riesgo
            1 -> {
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19, pero existe probabilidad de tenerlo")
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Sin embargo, a causa de que estuvo en contacto con una persona con COVID-19 podria haberse contagiado, es recomendable que haga el autodiagnóstico nuevamente cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor recuerde seguir las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("En caso de que presente cualquier tipo de síntoma, deberá realizar el autodiagnóstico para evitar llegar a una situación de gravedad.")
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
            //Sin ningún síntoma y con contacto con paciente covid y con al menos un factor de riesgo
            2 -> {
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerSubtitulo.setText("No presenta ningún sintoma de COVID-19, pero existe probabilidad de tenerlo.")
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Sin embargo, a causa de que estuvo en contacto con una persona con COVID-19 existe la posibilidad de haberse contagiado, tambien presenta al menos un antecedente que podría poner en riesgo su salud, es recomendable que haga el autodiagnóstico cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor recuerde seguir cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_red)
                //mensaje2Imagen.
                mensaje2Texto.setText("En caso de que presente cualquier síntoma, deberá realizar el autodiagnóstico para evitar llegar a una situación de gravedad.")
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
            //Sin ningún síntoma y sin contacto con paciente covid y con al menos un factor de riesgo
            3 -> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerTitulo.setText("SIN SINTOMAS COVID-19")
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerSubtitulo.setText("No presenta ningún sintoma relacionado con COVID-19 pero presenta factores de riesgo")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>no tiene síntomas compatibles con COVID-19</b>"))
                diagnosticoLinea2.setText("Sin embargo, tiene antecedentes que actuan como factores de riesgo en caso de contraer COVID-19, intente realizar el autodiagnóstico cada:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("4")
                diagnosticoHoraTexto.setText("DIAS")
                mensaje1Texto.setText("Por favor recuerde seguir cuidadosamente las siguientes recomendaciones:")
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
            //10 - Con al menos dos sintomas, al menos un factor de riesgo y contacto covid
            10-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y alto riesgo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 y alta probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus sintomas son agrabiados por sus antecedentes de riesgo y el contacto con una persona con COVID-19, realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //11 -> Con al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
            11-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas altamente compatibles con COVID-19 y alto riesgo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas altamente compatibles con COVID-19 y elevada probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus sintomas son muy específicos a COVID-19 y son agrabiados por sus antecedentes de riesgo y el contacto con una persona con COVID-19, realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //12 -> Con al menos dos sintomas, sin factor de riesgo y con contacto covid
            12 -> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo médio.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 y probabilidad de haberlo contraido.</b>"))
                diagnosticoLinea2.setText("Sus sintomas son agrabiados por el contacto reciente con una persona con COVID-19, realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por su seguridad y la de su familia, por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //13 -> Con al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y con contacto covid
            13-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas altamente compatibles con COVID-19 y probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas altamente compatibles con COVID-19 y elevada probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus síntomas son muy específicos a COVID-19 y son agraviados por el contacto reciente con una persona con COVID-19, realice nuevamente el autodiagnóstico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("24")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //14 - Con al menos dos sintomas, al menos un factor de riesgo y sin contacto covid
            14-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo moderado.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 y alguna probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus sintomas son agrabiados por sus antecedentes de riesgo, le recomendamos que realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y se programará un contacto entre Ud. y el personal de salud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_aislamiento_14)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //15 -> Con al menos dos sintomas incluido perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
            15-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas altamente compatibles con COVID-19 y probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas altamente compatibles con COVID-19 y elevada probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus síntomas son muy específicos a COVID-19 y son agraviados por algunos de sus antecedentes, realice nuevamente el autodiagnóstico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //16 -> Con al menos dos sintomas incluido perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
            16-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta algunos síntomas compatibles con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntomas compatibles con COVID-19 y alguna probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus síntomas son compatibles con COVID-19 y presenta bajo riesgo en caso de haberlo contraído, le recomendamos que realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y se programará un contacto entre Ud. y el personal de salud.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado el nayor tiempo posible, en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("Procure no utilizar el mismo baño que utilizan las demás personas con las que convive.")
            }
            //20 -> Con un sintoma, al menos un factor de riesgo y contacto covid
            20-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta un síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 y probabilidad de haberlo contraído.</b>"))
                diagnosticoLinea2.setText("Su síntoma es compatible con COVID-19 y el contacto con una persona con COVID-19 incrementa el riesgo de haberse contagiado, le recomendamos que realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y se programará un contacto entre Ud. y el personal de salud.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado el nayor tiempo posible, en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("Procure no utilizar el mismo baño que utilizan las demás personas con las que convive.")
            }
            //21 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y con contacto covid
            21-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta síntoma altamente compatible con COVID-19 y alto riesgo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntoma altamente compatible con COVID-19 y elevada probabilidad de haberse contagiado.</b>"))
                diagnosticoLinea2.setText("Su sintoma es específico a COVID-19 y se ve reforzado por el contacto reciente con una persona con COVID-19. Dados sus antedecentes de riesgo, realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //22 -> Con un sintoma, sin factor de riesgo y con contacto covid
            22 -> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntoma compatible con COVID-19 y probabilidad de haberlo contraido.</b>"))
                diagnosticoLinea2.setText("El síntoma informado se ve agrabiado por el contacto reciente con una persona con COVID-19, realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por su seguridad y la de su familia, por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
            }
            //23 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y con contacto covid
            23-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_red)
                headerImagen.setImageResource(R.drawable.ic_danger_red)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta síntoma altamente compatible con COVID-19 y probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntoma altamente compatible con COVID-19 y elevada probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus síntomas son muy específicos a COVID-19 y son agraviados por el contacto reciente con una persona con COVID-19, realice nuevamente el autodiagnóstico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Manténgase aislado en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("No utilice el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben permanecer en su vienda, pronto será sometido a pruebas que permitan confirmar el diagnostico.")
            }
            //24 -> Con un sintoma, al menos un factor de riesgo y sin contacto covid
            24-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD BAJA DE COVID-19")
                headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo moderado.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntoma compatible con COVID-19 y baja probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Sus antecedentes podrían poner en riesgo su salud en caso de contraer COVID-19, le recomendamos que realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_red)
                //mensaje2Imagen.
                mensaje2Texto.setText("En caso de que presente nuevos síntomas, realice el autodiagnóstico para evitar llegar a una situación de gravedad.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic__01_long_distance)
                mensaje4Texto.setText("Mantenga la distancia con otras personas, separándose de ellas por al menos 1 metro de distancia.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_handwash)
                mensaje5Texto.setText("Lávese las manos con agua y jabón, de manera frecuente y por lo menos por 20 segundos.")
            }
            //25 -> Con un sintoma entre perdida de olfato o gusto, al menos un factor de riesgo y sin contacto covid
            25-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD DE COVID-19")
                headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y mediana probabilidad de haberlo contraído.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene síntoma altamente compatible con COVID-19 y probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("Su síntoma es específico a COVID-19 y en caso de tenerlo sus antecedentes podrían poner en riesgo su salud, para evitarlo, realice nuevamente el autodiagnóstico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_bn)
                //mensaje2Imagen.
                mensaje2Texto.setText("Su diagnóstico a sido informado y en breve personal de salud se pondrá en contacto con Ud.")
                //mensaje 2
                mensaje3Layout.visibility = View.VISIBLE
                mensaje3Imagen.setImageResource(R.drawable.ic__13_face_mask)
                mensaje3Texto.setText("Haga uso de un barbijo que le cubra la nariz y la boca en todo momento.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("Si sus síntomas aumentan, aíslece en una habitación separada hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("Procure no utilizar el mismo baño que utilizan las demás personas con las que convive.")
                //mensaje 4
                mensaje6Layout.visibility = View.VISIBLE
                mensaje6Imagen.setImageResource(R.drawable.ic_cuarentena)
                mensaje6Texto.setText("Ud. y su familia deben salir lo menos posible de su vienda, pronto se podra confirmar su diagnostico.")
            }
            //26 -> Con un sintoma entre perdida de olfato o gusto, sin factor de riesgo y sin contacto covid
            26-> {
                headerLayout.setBackgroundResource(R.drawable.layout_bg_yellow)
                headerImagen.setImageResource(R.drawable.ic_warning_amarillo)
                headerTitulo.setText("POSIBILIDAD BAJA DE COVID-19")
                headerSubtitulo.setText("Presenta síntoma compatible con COVID-19 y riesgo bajo.")
                diagnosticoNombre.setText(Html.fromHtml("$nombreCompleto, <b>tiene un síntoma compatible con COVID-19 y baja probabilidad de tenerlo.</b>"))
                diagnosticoLinea2.setText("El síntoma es compatible con COVID-19 y presenta bajo riesgo en caso de haberlo contraído, le recomendamos que realice nuevamente el autodiagnostico en:")
                diagnosticoHoraImagen.setImageResource(R.drawable.ic_baseline_access_time_24)
                diagnosticoHoraLiteral.setText("48")
                diagnosticoHoraTexto.setText("HORAS")
                mensaje1Texto.setText("Por favor siga cuidadosamente las siguientes recomendaciones:")
                //mensaje 1
                mensaje2Layout.visibility = View.VISIBLE
                mensaje2Imagen.setImageResource(R.drawable.ic_danger_red)
                //mensaje2Imagen.
                mensaje2Texto.setText("En caso de que presente nuevos síntomas, realice el autodiagnóstico para evitar llegar a una situación de gravedad.")
                //mensaje 3
                mensaje4Layout.visibility = View.VISIBLE
                mensaje4Imagen.setImageResource(R.drawable.ic_aislamiento)
                mensaje4Texto.setText("De ser posible, aíslese en una habitación independiente hasta que se pueda confirmar su diagnóstico.")
                //mensaje 4
                mensaje5Layout.visibility = View.VISIBLE
                mensaje5Imagen.setImageResource(R.drawable.ic_diarrhea)
                mensaje5Texto.setText("Procure no utilizar el mismo baño que utilizan las demás personas con las que convive.")
            }
            else -> Toast.makeText(this, "Valor de nivel de riesgo no reconocido", Toast.LENGTH_SHORT).show()
        }

        btFinalizar1.setOnClickListener() {
            if (verificaInternet()) {
                val diaActual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val mesActual = Calendar.getInstance().get(Calendar.MONTH) + 1
                val anoActual = Calendar.getInstance().get(Calendar.YEAR)
                val horaActual = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val minutoActual = Calendar.getInstance().get(Calendar.MINUTE)
                val segundoActual = Calendar.getInstance().get(Calendar.SECOND)
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
                    db.collection("usuarios").document(Variables.IDUNICO).collection("diagnosticos").document("autodiagnostico").set(
                        hashMapOf                            (
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
                    )
                    Log.i("Cuidarnos", "Se registraron los datos del diagnostico en la nube")
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
                    "No se pueden registrar los datos en la nuve por que no se tiene conexion a internet")
                Toast.makeText(this, "Por favor conectese a Internet para enviar el diagnóstico.", Toast.LENGTH_SHORT).show()
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
}