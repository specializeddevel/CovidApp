package org.csrabolivia.covidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {



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


    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(300)
        notification()
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("InitScreen", "Integración con Firebase completada")
        analytics.logEvent("InitScreen", bundle)

        setContentView(R.layout.activity_main)
    }
}