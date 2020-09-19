package org.csrabolivia.covidapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var datePicker: DatePickerHelper

    /*val listener= View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btContinuar -> {
                // Do some work here
                Log.d("Depuracion", "ingreso a click")
                Toast.makeText(this, "Clicked 1", Toast.LENGTH_SHORT).show()
            }
        }
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(2000)
        notification()
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        datePicker = DatePickerHelper(this)
        btContinuar.setOnClickListener(this::escuchar)
        textFieldDateBirthday2.setOnClickListener(this::escucharFecha)

        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("InitScreen", "Integración con Firebase completada")
        analytics.logEvent("InitScreen", bundle)

        val itemsGender = listOf("Masculino", "Femenino" )
        val adapterGender = ArrayAdapter(this, R.layout.list_item, itemsGender)
        (textFieldGender.editText as? AutoCompleteTextView)?.setAdapter(adapterGender)

        val itemsEstadoCivil = listOf("Soltero(a)", "Casado(a)", "Conviviente", "Divorciado(a)", "Separado(a)", "Viudo(a)" )
        val adapterEstadoCivil = ArrayAdapter(this, R.layout.list_item, itemsEstadoCivil)
        (textFieldCivilState.editText as? AutoCompleteTextView)?.setAdapter(adapterEstadoCivil)

        val itemsMunicipio = listOf("Montero", "Otro" )
        val adapterMunicipio = ArrayAdapter(this, R.layout.list_item, itemsMunicipio)
        (textFieldMunicipio.editText as? AutoCompleteTextView)?.setAdapter(adapterMunicipio)

        val itemsCiudad = listOf("Montero", "Otro" )
        val adapterCiudad = ArrayAdapter(this, R.layout.list_item, itemsCiudad)
        (textFieldCiudad.editText as? AutoCompleteTextView)?.setAdapter(adapterCiudad)

        val itemsBarrio = listOf("24 de septiembre","27 de mayo","3 de Mayo","30 de Noviembre","Asahi","Bolívar","El Carmen","Fabril","Kennedy","La Cruz","Monteverde","San Juan XXIII","Santa Barbara","Santa Tereza","Villa Cbba.","Villa Verde","Villa Virginia","Virgen de Cotoca","Zona Central","Otro...")
        val adapterBarrio = ArrayAdapter(this, R.layout.list_item, itemsBarrio)
        (textFieldBarrio.editText as? AutoCompleteTextView)?.setAdapter(adapterBarrio)
   }

    private fun escuchar(v: View) {
        //Log.d("Depuracion", "ingreso a click")
        var intent = Intent(this, AccesoAplicacionActivity::class.java)
        intent.putExtra("NOMBRE", textFieldNames.editText?.text.toString())
        startActivity(intent)
    }

    private fun escucharFecha(v: View) {
        showDatePickerDialog()
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                textFieldDateBirthday2.setText("${dayStr}-${monthStr}-${year}")
            }
        })
    }

    private fun notification(){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener{

            it.result?.token?.let{
                Log.d("Depuracion","El Id unico es: ${it}")
            }
        }
        //suscripcion a un tema
        FirebaseMessaging.getInstance().subscribeToTopic("COVID")

        //recuperacion de info de una push
        val url: String? = intent.getStringExtra("url")
        url?.let {
            Log.d("Depuracion", "Ha llegado esta informacion en un push: ${it}")
        }
    }
}