package com.atul.foodzone.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.adapter.CartAdapter
import com.atul.foodzone.database.DishDatabase
import com.atul.foodzone.database.DishEntity
import com.atul.foodzone.model.Details
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var txtResOrdername: TextView
    lateinit var btnPlaceOrder: Button
    lateinit var cartRecycler: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressbar: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    var OrderList = arrayListOf<Details>()
    var restaurant_id: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()
        txtResOrdername = findViewById(R.id.txtResOrderName)
        cartRecycler = findViewById(R.id.cartRecycler)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        progressLayout = findViewById(R.id.progressLayout)
        progressbar = findViewById(R.id.progressBar)

        layoutManager = LinearLayoutManager(this)

        val ResName = sharedPreferences.getString("res_name", "Amul")
        txtResOrdername.text = "Ordering From : $ResName"

        setUpCart()

        var sum = 0
        for (i in 0 until OrderList.size) {
            sum += OrderList[i].CostForOne.toInt()
        }
        btnPlaceOrder.text = "Place Order Rs. $sum"

        btnPlaceOrder.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this)) {
                progressLayout.visibility = View.GONE

                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/place_order/fetch_result/"

                val jsonParams = JSONObject()
                jsonParams.put(
                    "user_id",
                    this@CartActivity.getSharedPreferences(
                        getString(R.string.preference_name),
                        Context.MODE_PRIVATE
                    ).getString(
                        "user_id",
                        "null"
                    ) as String
                )
                jsonParams.put("restaurant_id", restaurant_id)
                jsonParams.put("total_cost", sum.toString())
                val foodArray = JSONArray()
                for (i in 0 until OrderList.size) {
                    val foodId = JSONObject()
                    foodId.put("food_item_id", OrderList[i].id)
                    foodArray.put(i, foodId)
                }
                jsonParams.put("food", foodArray)
                val JsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val response = it.getJSONObject("data")
                            val success = response.getBoolean("success")
                            if (success) {
                                val ans = DishDBClear(applicationContext).execute().get()
                                OrderList.clear()

                                val dialog =
                                    Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                                dialog.setContentView(R.layout.cart_order_confirm)
                                dialog.create()
                                dialog.show()
                                dialog.setCancelable(false)
                                val btnOk = dialog.findViewById<Button>(R.id.btnOk)
                                btnOk.setOnClickListener {
                                    dialog.dismiss()
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    ActivityCompat.finishAffinity(this)
                                }
                            } else {
                                val dialog =
                                    Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                                dialog.setContentView(R.layout.cart_order_confirm)
                                dialog.create()
                                dialog.show()
                                dialog.setCancelable(false)
                                val btnOk = dialog.findViewById<Button>(R.id.btnOk)
                                btnOk.setOnClickListener {
                                    dialog.dismiss()
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    ActivityCompat.finishAffinity(this)
                                }
                            }
                        } catch (e: JSONException) {
                            progressLayout.visibility = View.VISIBLE
                            e.printStackTrace()
                            Toast.makeText(this, "Some Json Error Occurred!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }, Response.ErrorListener {
                        progressLayout.visibility = View.VISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "d273f98a222b3e"
                            return headers
                        }
                    }
                queue.add(JsonObjectRequest)
            }

        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val async = DishDBClear(applicationContext).execute().get()
        OrderList.clear()
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
    }

    class DishDBClear(val context: Context) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            db.dishDao().deleteAllContents()
            db.close()
            return true
        }
    }

    class GetAllDishDB(val context: Context) : AsyncTask<Void, Void, List<DishEntity>>() {
        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()
        override fun doInBackground(vararg p0: Void?): List<DishEntity> {
            return db.dishDao().getAllDishes()
        }

    }

    fun setUpCart() {
        var totalCost = 0
        val dbList = GetAllDishDB(applicationContext).execute().get()
        for (i in dbList) {
            val Detail = Details(
                i.id,
                i.DishName,
                i.costOfDish,
                i.res_id
            )
            restaurant_id = Detail.ResId
            totalCost = totalCost + (Detail.CostForOne.toInt() as Int)
            OrderList.add(Detail)
        }
        if (OrderList.isEmpty()) {
            progressLayout.visibility = View.VISIBLE
        } else {
            progressLayout.visibility = View.GONE
        }
        recyclerAdapter = CartAdapter(this, OrderList)
        cartRecycler.layoutManager = layoutManager
        cartRecycler.adapter = recyclerAdapter
    }

    override fun onDestroy() {
        val async = DishDBClear(applicationContext).execute().get()
        OrderList.clear()
        super.onDestroy()
    }
}

