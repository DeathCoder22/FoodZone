package com.atul.foodzone.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etEnterMob : EditText
    lateinit var etEnterPass : EditText
    lateinit var btnLogin : Button
    lateinit var txtForgotPass : TextView
    lateinit var txtSignUp : TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEnterMob = findViewById(R.id.etEnterMob)
        etEnterPass = findViewById(R.id.etEnterPass)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPass = findViewById(R.id.txtForgotPass)
        txtSignUp = findViewById(R.id.txtSignUp)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_name),Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
        if(isLoggedIn){
            val gotoHome = Intent(this@LoginActivity,HomeActivity::class.java)
            startActivity(gotoHome)
            finish()
        }


        /*step 1 -> set button and req through volley
        * step 2-> set forgot password page
        * step 3-> set sign up page*/

        btnLogin.setOnClickListener {
            val MobNum = etEnterMob.text.toString()
            val passWord = etEnterPass.text.toString()

            if(ConnectionManager().checkConnectivity(this@LoginActivity)){
                if(MobNum.length==10 && passWord.length>=4) {
                    val queueLogin = Volley.newRequestQueue(this@LoginActivity)
                    val urlLogin = "http://13.235.250.119/v2/login/fetch_result/"
                    etEnterMob.error = null
                    etEnterPass.error = null
                    val jsonParamsLogin = JSONObject()
                    jsonParamsLogin.put("mobile_number",etEnterMob.text.toString())
                    jsonParamsLogin.put("password",etEnterPass.text.toString())
                    val jsonRequest = object : JsonObjectRequest(
                        Method.POST,
                        urlLogin,
                        jsonParamsLogin,
                        Response.Listener {
                            //Responses to be handled here
                            try {
                                val response = it.getJSONObject("data")
                                val success = response.getBoolean("success")
                                println("success is $success")
                                if (success) {
                                    val dataObj = response.getJSONObject("data")
                                    if (MobNum == dataObj.getString("mobile_number")) {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Logged in successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        sharedPreferences.edit().putBoolean("isLoggedIn", true)
                                            .apply()
                                        sharedPreferences.edit().putString("user_mobile_number", MobNum)
                                            .apply()
                                        val intent = Intent(this,HomeActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "The entered mobile number and password does not match",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "The entered mobile number and password does not match",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(this, "Json error Occurred!!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        Response.ErrorListener {
                            //errors to be handled here
                            Toast.makeText(
                                this@LoginActivity,
                                "Check your Internet Connection!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "d273f98a222b3e"
                            return headers
                        }
                    }
                    queueLogin.add(jsonRequest)
                }else{
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Credentials Error!!!")
                    dialog.setMessage("Please Enter the required fields correctly.")
                    dialog.setPositiveButton("OK"){text,Listener->}
                    dialog.create()
                    dialog.show()
                }
            }else{
                //a dialog box fo no internet
                    val dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection not found !!..........If you want to continue, click on open settings :)")
                    dialog.setPositiveButton("Open Settings"){text,listener->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit App"){text,listener->
                        ActivityCompat.finishAffinity(this@LoginActivity)
                    }
                    dialog.create()
                    dialog.show()
                }
            }
            txtSignUp.setOnClickListener {
                val goToRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(goToRegister)
            }
            txtForgotPass.setOnClickListener {
                val activity = Intent(this,ForgotPassword::class.java)
                startActivity(activity)
            }
        }
    }
