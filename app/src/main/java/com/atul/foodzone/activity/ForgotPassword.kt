package com.atul.foodzone.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var etMob : EditText
    lateinit var etEmail : EditText
    lateinit var btnNext : Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_name),Context.MODE_PRIVATE)
        etMob = findViewById(R.id.etMob)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {
            val mob = etMob.text.toString()
            val Email = etEmail.text.toString()
            if(mob.length==10 && isValidEmail(Email)==true) {
                if (ConnectionManager().checkConnectivity(this)) {
                    etMob.error = null
                    etEmail.error = null
                    val queue = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", etMob.text.toString())
                    jsonParams.put("email", etEmail.text.toString())
                    val jsonRequest = object : JsonObjectRequest(Method.POST,url,jsonParams,
                        Response.Listener {
                            try{
                                val response = it.getJSONObject("data")
                                val success = response.getBoolean("success")
                                if(success){
                                     val firstTry = response.getBoolean("first_try")
                                    sharedPreferences.edit().putString("user_mobile_number",etMob.text.toString()).apply()
                                    sharedPreferences.edit().putString("user_email",etEmail.text.toString()).apply()
                                        if (firstTry) {
                                            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                                            builder.setTitle("Information")
                                            builder.setMessage("Please check your registered Email for the OTP.")
                                            builder.setCancelable(false)
                                            builder.setPositiveButton("Ok") { _, _ ->
                                                val intent = Intent(
                                                    this,
                                                    ResetPassword::class.java
                                                )
                                                intent.putExtra("user_mobile", mob)
                                                startActivity(intent)
                                            }
                                            builder.create().show()
                                        } else {
                                            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                                            builder.setTitle("Information")
                                            builder.setMessage("Please refer to the previous email for the OTP if previously reset your password within 24 hours.")
                                            builder.setCancelable(false)
                                            builder.setPositiveButton("Ok") { _, _ ->
                                                val intent = Intent(
                                                    this@ForgotPassword,
                                                    ResetPassword::class.java
                                                )
                                                intent.putExtra("user_mobile", mob)
                                                startActivity(intent)
                                            }
                                            builder.create().show()
                                        }
                                }else{
                                    Toast.makeText(this@ForgotPassword,"Check your internet connection and try again!!",Toast.LENGTH_LONG).show()
                                }}
                            catch(e : JSONException){
                                Toast.makeText(this@ForgotPassword,"Some error Occurred",Toast.LENGTH_LONG).show()
                            }
                    },
                        Response.ErrorListener {
                         Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "d273f98a222b3e"
                            return headers
                        }
                    }
                    queue.add(jsonRequest)
                } else {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Network Error!!")
                    dialog.setMessage("Check your Internet Connection")
                    dialog.setPositiveButton("OK") { text, Listener -> }
                }
            }else{
                Toast.makeText(this,"Please fill the required fields correctly",
                    Toast.LENGTH_LONG).show()
            }
        }

    }fun isValidEmail(email : String):Boolean{
        if(email == null){
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}