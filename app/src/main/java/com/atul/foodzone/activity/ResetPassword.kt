package com.atul.foodzone.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {
    lateinit var etOTP : EditText
    lateinit var etPass : EditText
    lateinit var etConPass : EditText
    lateinit var btnReset : Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE)

        etOTP = findViewById(R.id.etOTP)
        etPass = findViewById(R.id.etPass)
        etConPass = findViewById(R.id.etConPass)
        btnReset = findViewById(R.id.btnReset)

        btnReset.setOnClickListener {
            val pass = etPass.text.toString()
            val conPass = etConPass.text.toString()
            val mobile = sharedPreferences.getString("user_mobile_number","0000000000")
            if(pass == conPass){
                if(ConnectionManager().checkConnectivity(this)){
                    etPass.error = null
                    etConPass.error = null
                    etOTP.error = null
                    val queue = Volley.newRequestQueue(this)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                    val jsonparams = JSONObject()
                    jsonparams.put("mobile_number",mobile)
                    jsonparams.put("password",etPass.text.toString())
                    jsonparams.put("otp",etOTP.text.toString())

                    val jsonRequest = object : JsonObjectRequest(Method.POST,url,jsonparams,Response.Listener {
                        try{
                            val response = it.getJSONObject("data")
                            val success = response.getBoolean("success")
                            val successmsg = response.getString("successMessage")
                            if(success){
                                    Toast.makeText(this,successmsg,Toast.LENGTH_SHORT).show()
                                     val intent = Intent(this,LoginActivity::class.java)
                                    startActivity(intent)
                            }else{
                                Toast.makeText(this,"Check your Internet Connection",Toast.LENGTH_SHORT).show()
                            }
                        }catch(e : JSONException){
                            Toast.makeText(this,"Some Error Occurred!",Toast.LENGTH_SHORT).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(this,"Some Error Occurred!",Toast.LENGTH_SHORT).show()
                        }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "d273f98a222b3e"
                            return headers
                        }
                    }
                    queue.add(jsonRequest)
                }else{
                    Toast.makeText(this,"Check your Internet Conectivity",Toast.LENGTH_LONG).show()
                }
            }else{
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Password error")
                dialog.setMessage("The Password fields do not match!")
                dialog.setPositiveButton("OK"){text,Listener->
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}