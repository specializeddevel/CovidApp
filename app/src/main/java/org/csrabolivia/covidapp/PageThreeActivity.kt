package org.csrabolivia.covidapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_page_one.*
import kotlinx.android.synthetic.main.activity_page_three.*
import kotlinx.android.synthetic.main.activity_page_two.*
import org.json.JSONObject

class PageThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_three)

        checkBoxTerminos.isChecked = false

        btAtras2.setOnClickListener(){
            onBackPressed()
        }



        btContinuar3.setOnClickListener(){
            onBackPressed()
        }

        checkBoxTerminos.setOnClickListener(){
            closeKeyBoard()
            btContinuar3.isEnabled = checkBoxTerminos.isChecked
        }

        btContinuar3.setOnClickListener(){

        }
    }


    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}