package com.example.labproj

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    public var APIKEY = "493cc26f343881bec850558c6e93c041"
    public var site= "https://api.openweathermap.org/data/2.5/weather?q=%D0%9A%D0%BE%D0%B2%D0%B5%D0%BB%D1%8C&units=metric&appid=493cc26f343881bec850558c6e93c041&lang=ru"
    lateinit var temptext:TextView
    var mRequestQueue: RequestQueue?=null
    lateinit var humiditytext:TextView
    lateinit var windspeedtext:TextView
    lateinit var pressuretext:TextView
    lateinit var sPref: SharedPreferences
    lateinit var cityname:TextView

    var citynamel =""
    @SuppressLint("MissingInflatedId", "ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestQueue = Volley.newRequestQueue(this)
        setContentView(R.layout.activity_main)
        humiditytext = findViewById(R.id.humiditytext)
        windspeedtext = findViewById(R.id.windspeedtext)
        pressuretext = findViewById(R.id.pressuretext)
        temptext = findViewById(R.id.temptext)
        cityname = findViewById(R.id.cityname)
        loadcityname()
        val inm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        cityname.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                inm.hideSoftInputFromWindow(cityname.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
                savecityname()
                getWeather("https://api.openweathermap.org/data/2.5/weather?q=${citynamel}&units=metric&appid=493cc26f343881bec850558c6e93c041&lang=ru")
            }
            true
        }

        getWeather("https://api.openweathermap.org/data/2.5/weather?q=${citynamel}&units=metric&appid=493cc26f343881bec850558c6e93c041&lang=ru")
    }
    private fun loadcityname(){
        sPref = getPreferences(MODE_PRIVATE)
        citynamel = sPref.getString("SAVED_TEXT", null).toString()
        cityname.text = citynamel
    }
    private fun loadcitynametext():String{
        sPref = getPreferences(MODE_PRIVATE)
        citynamel = sPref.getString("SAVED_TEXT", null).toString()
        return citynamel
    }
    private fun savecityname(){
        sPref = getPreferences(MODE_PRIVATE)
        var ed:SharedPreferences.Editor = sPref.edit()
        ed.putString("SAVED.TEXT", cityname.text.toString())
        ed.commit()
    }
    fun getWeather(site:String){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, site ,null,
            { response ->
                var main = response.getJSONObject("main")
                cityname.text = loadcitynametext()
                temptext.text="${main.getInt("temp")}°"
                humiditytext.text="${main.getInt("humidity")}%"
                windspeedtext.text ="${response.getJSONObject("wind").getInt("speed")} м/c"
                pressuretext.text="${main.getInt("pressure")} гПа"
            },
            {error ->
                Toast.makeText(this,"Помилка при загрузці!", Toast.LENGTH_LONG).show()

            })
        mRequestQueue!!.add(jsonObjectRequest)

    }
}