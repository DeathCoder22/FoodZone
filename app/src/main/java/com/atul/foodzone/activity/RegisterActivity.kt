package com.atul.foodzone.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var etName : EditText
    lateinit var etEmail : EditText
    lateinit var etMobNum : EditText
    lateinit var etAddress : EditText
    lateinit var etPass : EditText
    lateinit var etConPass : EditText
    lateinit var btnRegister : Button
    lateinit var Toolbar : Toolbar
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobNum = findViewById(R.id.etMobNum)
        etAddress = findViewById(R.id.etAddress)
        etPass = findViewById(R.id.etPass)
        etConPass = findViewById(R.id.etConPass)
        btnRegister = findViewById(R.id.btnRegister)
        Toolbar = findViewById(R.id.Toolbar)
        setUpToolBar()

        sharedPreferences = getSharedPreferences(getString(R.string.preference_name),Context.MODE_PRIVATE)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val mobile = etMobNum.text.toString()
            val address = etAddress.text.toString()
            val password = etPass.text.toString()
            val conPassword = etConPass.text.toString()

            if(password == conPassword){
                if(name.length >= 3){
                    if(isValidEmail(email)){
                         if(mobile.length >= 10){

                            if(ConnectionManager().checkConnectivity(this@RegisterActivity)){
                                etName.error = null
                                etMobNum.error = null
                                etConPass.error = null
                                etPass.error = null
                                etAddress.error = null
                                etEmail.error = null
                                val queueRegister = Volley.newRequestQueue(this@RegisterActivity)
                                val urlRequest = "http://13.235.250.119/v2/register/fetch_result"
                                val jsonParams = JSONObject()
                                jsonParams.put("name", name)
                                jsonParams.put("mobile_number", mobile)
                                jsonParams.put("password", password)
                                jsonParams.put("address", address)
                                jsonParams.put("email", email)

                                val jsonObjectRequest = object : JsonObjectRequest(
                                    Request.Method.POST,
                                    urlRequest,
                                    jsonParams,
                                    Response.Listener {
                                        try {
                                            val data = it.getJSONObject("data")
                                            val success = data.getBoolean("success")
                                            if (success) {
                                                val response = data.getJSONObject("data")
                                                sharedPreferences.edit()
                                                    .putString("user_id", response.getString("user_id")).apply()
                                                val dialog = AlertDialog.Builder(this)
                                                dialog.setTitle("Impoertant")
                                                dialog.setMessage("Your User Id is ${response.getString("user_id")}.............Kindly note this User Id for future reference.")
                                                dialog.setPositiveButton("OK"){_,_->}
                                                dialog.create()
                                                dialog.show()
                                                sharedPreferences.edit()
                                                    .putString("user_name", response.getString("name")).apply()
                                                sharedPreferences.edit()
                                                    .putString(
                                                        "user_mobile_number",
                                                        response.getString("mobile_number")
                                                    )
                                                    .apply()
                                                sharedPreferences.edit().putString("user_address", response.getString("address")).apply()
                                                sharedPreferences.edit()
                                                    .putString("user_email", response.getString("email")).apply()
                                                startActivity(
                                                    Intent(
                                                        this@RegisterActivity,
                                                        HomeActivity::class.java
                                                    )
                                                )
                                                finish()
                                            } else {
                                                val errorMessage = data.getString("errorMessage")
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    errorMessage,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } catch (e: Exception){
                                            e.printStackTrace()
                                        }
                                    },
                                    Response.ErrorListener {
                                        Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                                    }
                                ){
                                    override fun getHeaders(): MutableMap<String, String> {
                                        val headers = HashMap<String,String>()
                                        headers["Content-type"] = "application/json"
                                        headers["token"] = "d273f98a222b3e"
                                        return headers
                                    }
                                }
                                queueRegister.add(jsonObjectRequest)
                            }
                         }else{
                             val dialog = AlertDialog.Builder(this@RegisterActivity)
                             dialog.setTitle("Mobile Number Error")
                             dialog.setMessage("The Entered Mobile Number is either Incorrect or not an indian Mobile Number")
                             dialog.setPositiveButton("OK"){text,Listener->}
                             dialog.create()
                             dialog.show()
                         }
                    }else{
                        val dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("Email Error!!!")
                        dialog.setMessage("The Entered Email ID is Incorrect")
                        dialog.setPositiveButton("OK"){text,Listener->}
                        dialog.create()
                        dialog.show()
                    }
                }else{
                    val dialog = AlertDialog.Builder(this@RegisterActivity)
                    dialog.setTitle("Name Error!!!")
                    dialog.setMessage("The Entered Name is not as per requirements")
                    dialog.setPositiveButton("OK"){text,Listener->

                    }
                    dialog.create()
                    dialog.show()
                }
            }else{
                val dialog = AlertDialog.Builder(this@RegisterActivity)
                dialog.setTitle("Password!!!")
                dialog.setMessage("The Password and Confirm Password fields do not match")
                dialog.setPositiveButton("OK"){
                    text,Listener->//Do nothing now
                }
                dialog.create()
                dialog.show()
            }
        }
    }

    fun setUpToolBar(){
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "REGISTER"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun isValidEmail(email : String?):Boolean{
        if(email == null){
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}

/*
    package com.atul.foodrunner.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodrunner.R
import com.internshala.foodrunner.util.ConnectionManager
import com.internshala.foodrunner.util.REGISTER
import com.internshala.foodrunner.util.SessionManager
import com.internshala.foodrunner.util.Validations
import org.json.JSONObject
import java.lang.Exception

/* The registration activity is responsible for registering the users to the app
* This will send the fields to server and the user will get registered if all the fields were correct.
* The user receives response in the form of JSON
* If the login is true, the user is navigated to the dashboard else appropriate error message is displayed*/

class RegisterActivity : AppCompatActivity() {

lateinit var toolbar: Toolbar
lateinit var btnRegister: Button
lateinit var etName: EditText
lateinit var etPhoneNumber: EditText
lateinit var etPassword: EditText
lateinit var etEmail: EditText
lateinit var etAddress: EditText
lateinit var etConfirmPassword: EditText
lateinit var progressBar: ProgressBar
lateinit var rlRegister: RelativeLayout
lateinit var sharedPreferences: SharedPreferences
lateinit var sessionManager: SessionManager

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_register)

toolbar = findViewById(R.id.toolbar)
setSupportActionBar(toolbar)
supportActionBar?.title = "Register Yourself"
supportActionBar?.setHomeButtonEnabled(true)
supportActionBar?.setDisplayHomeAsUpEnabled(true)
toolbar.setTitleTextAppearance(this, R.style.PoppinsTextAppearance)
sessionManager = SessionManager(this@RegisterActivity)
sharedPreferences = this@RegisterActivity.getSharedPreferences(sessionManager.PREF_NAME, sessionManager.PRIVATE_MODE)
rlRegister = findViewById(R.id.rlRegister)
etName = findViewById(R.id.etName)
etPhoneNumber = findViewById(R.id.etPhoneNumber)
etEmail = findViewById(R.id.etEmail)
etPassword = findViewById(R.id.etPassword)
etConfirmPassword = findViewById(R.id.etConfirmPassword)
etAddress = findViewById(R.id.etAddress)
btnRegister = findViewById(R.id.btnRegister)
progressBar = findViewById(R.id.progressBar)

rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE


btnRegister.setOnClickListener {
rlRegister.visibility = View.INVISIBLE
progressBar.visibility = View.VISIBLE
if (Validations.validateNameLength(etName.text.toString())) {
etName.error = null
if (Validations.validateEmail(etEmail.text.toString())) {
etEmail.error = null
if (Validations.validateMobile(etPhoneNumber.text.toString())) {
etPhoneNumber.error = null
if (Validations.validatePasswordLength(etPassword.text.toString())) {
etPassword.error = null
if (Validations.matchPassword(
etPassword.text.toString(),
etConfirmPassword.text.toString()
)
) {
etPassword.error = null
etConfirmPassword.error = null
if (ConnectionManager().isNetworkAvailable(this@RegisterActivity)) {
sendRegisterRequest(
etName.text.toString(),
etPhoneNumber.text.toString(),
etAddress.text.toString(),
etPassword.text.toString(),
etEmail.text.toString()
)
} else {
rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE
Toast.makeText(this@RegisterActivity, "No Internet Connection", Toast.LENGTH_SHORT)
.show()
}
} else {
rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE
etPassword.error = "Passwords don't match"
etConfirmPassword.error = "Passwords don't match"
Toast.makeText(this@RegisterActivity, "Passwords don't match", Toast.LENGTH_SHORT)
.show()
}
} else {
rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE
etPassword.error = "Password should be more than or equal 4 digits"
Toast.makeText(
this@RegisterActivity,
"Password should be more than or equal 4 digits",
Toast.LENGTH_SHORT
).show()
}
} else {
rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE
etPhoneNumber.error = "Invalid Mobile number"
Toast.makeText(this@RegisterActivity, "Invalid Mobile number", Toast.LENGTH_SHORT).show()
}
} else {
rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE
etEmail.error = "Invalid Email"
Toast.makeText(this@RegisterActivity, "Invalid Email", Toast.LENGTH_SHORT).show()
}
} else {
rlRegister.visibility = View.VISIBLE
progressBar.visibility = View.INVISIBLE
etName.error = "Invalid Name"
Toast.makeText(this@RegisterActivity, "Invalid Name", Toast.LENGTH_SHORT).show()
}
}

}

private fun sendRegisterRequest(name: String, phone: String, address: String, password: String, email: String) {

val queue = Volley.newRequestQueue(this)

{
override fun getHeaders(): MutableMap<String, String> {
val headers = HashMap<String, String>()
headers["Content-type"] = "application/json"

/*The below used token will not work, kindly use the token provided to you in the training*/
headers["token"] = "9bf534118365f1"
return headers
}
}
queue.add(jsonObjectRequest)
}

override fun onSupportNavigateUp(): Boolean {
Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
onBackPressed()
return true
}
}

...........

object Validations {
fun validateMobile(mobile: String): Boolean {
return mobile.length == 10
}

fun validatePasswordLength(password: String): Boolean {
return password.length >= 4
}

fun validateNameLength(name: String): Boolean {
return name.length >= 3
}

fun matchPassword(pass: String, confirmPass: String): Boolean {
return pass == confirmPass
}

fun validateEmail(email: String): Boolean {
return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
}
}

-----

class SessionManager(context: Context) {

var PRIVATE_MODE = 0
val PREF_NAME = "FoodApp"

val KEY_IS_LOGGEDIN = "isLoggedIn"
var pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
var editor = pref.edit()

fun setLogin(isLoggedIn: Boolean){
editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn)
editor.apply()
}


fun isLoggedIn(): Boolean {
return pref.getBoolean(KEY_IS_LOGGEDIN, false)
}

}


    */