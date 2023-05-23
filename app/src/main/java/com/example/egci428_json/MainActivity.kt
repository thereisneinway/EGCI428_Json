package com.example.egci428_json

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.io.IOException
import okhttp3.*

class MainActivity : AppCompatActivity() {

    private val jsonURL: String = "https://egci428-d78f6-default-rtdb.firebaseio.com/movies/1.json"
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)

        fun fetchJson(){
            val request = Request.Builder()
                .url(jsonURL)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }

                        //println(response.body!!.string())
                        val body = response.body!!.string()
                        if(body == null) return@use

                        val gson = GsonBuilder().create()
                        val movieName = gson.fromJson(body, Movie::class.java)
                        textView.text = movieName.name
                    }
                }
            })
        }

        button.setOnClickListener {
            fetchJson()
        }

    }
}