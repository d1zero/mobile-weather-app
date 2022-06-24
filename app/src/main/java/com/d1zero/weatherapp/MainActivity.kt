package com.d1zero.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.cdimascio.dotenv.dotenv
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {
    private var cityInput: EditText? = null
    private var mainBtn: Button? = null
    private var resultInfo: TextView? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        cityInput = findViewById(R.id.cityInput)
        mainBtn = findViewById(R.id.mainBtn)
        resultInfo = findViewById(R.id.resultInfo)

        mainBtn?.setOnClickListener {
            if (cityInput?.text?.toString()?.trim()?.equals("")!!)
                Toast.makeText(this, "Введите город", Toast.LENGTH_LONG).show()
            else {
                val city: String = cityInput!!.text.toString()
                val key = dotenv["API_KEY"]
                val url =
                    "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=" +
                            "$key&units=metric&lang=ru"

                val apiResponse = URL(url).readText()

                val weatherDescription = JSONObject(apiResponse)
                    .getJSONArray("weather").getJSONObject(0)
                    .getString("description")

                val temperature = JSONObject(apiResponse)
                    .getJSONObject("main").getString("temp")

                resultInfo?.text = "Температура $temperature\n$weatherDescription"
            }
        }
    }
}