package com.atul.foodzone.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.adapter.DetailsRecyclerAdapter
import com.atul.foodzone.database.DishDatabase
import com.atul.foodzone.database.DishEntity
import com.atul.foodzone.database.FavouriteDatabase
import com.atul.foodzone.database.FavouriteEntity
import com.atul.foodzone.model.Details
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONException

class RestaurantDetails : AppCompatActivity() {

    lateinit var Toolbar: androidx.appcompat.widget.Toolbar
    lateinit var imgButton: ImageButton
    lateinit var DetailsRecycler: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var btnCart: Button
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DetailsRecyclerAdapter
    lateinit var sharedPreferences: SharedPreferences
    var DishList = arrayListOf<Details>()
    var orderList = arrayListOf<Details>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE)
        Toolbar = findViewById(R.id.Toolbar)
        btnCart = findViewById(R.id.btnCart)
        imgButton = findViewById(R.id.imgButton)
        DetailsRecycler = findViewById(R.id.DetailsRecycler)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(this)
        btnCart.visibility = View.GONE

        val restaurant_id = intent.getStringExtra("id")
        val restaurant_name = intent.getStringExtra("resname")
        val restaurant_rating = intent.getStringExtra("resrating")
        val restaurant_cost = intent.getStringExtra("rescost")
        val restaurant_image = intent.getStringExtra("imageurl")

        val favouriteEntity = FavouriteEntity(
            restaurant_id.toString(),
            restaurant_name.toString(),
            restaurant_rating.toString(),
            restaurant_cost.toString(),
            restaurant_image.toString()
        )

        if (FavouriteDB(applicationContext, favouriteEntity, 3).execute().get()) {
            imgButton.setImageResource(R.drawable.ic_img_button_after)
        } else {
            imgButton.setImageResource(R.drawable.ic_img_button)
        }

        imgButton.setOnClickListener {
            if (!(FavouriteDB(applicationContext, favouriteEntity, 3).execute().get())) {
                val async = FavouriteDB(applicationContext, favouriteEntity, 1).execute()
                val ans = async.get()
                if (ans) {
                    Toast.makeText(this, "Restaurant added to Favourites", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
                imgButton.setImageResource(R.drawable.ic_img_button_after)

            } else {
                val async = FavouriteDB(applicationContext, favouriteEntity, 2).execute()
                val ans = async.get()
                if (ans) {
                    Toast.makeText(this, "Restaurant added to Favourites", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
                imgButton.setImageResource(R.drawable.ic_img_button)
            }
        }
        val Resname = intent.getStringExtra("resname")
        sharedPreferences.edit().putString("res_name", Resname).apply()
        setSupportActionBar(Toolbar)
        supportActionBar?.title = Resname
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        if (ConnectionManager().checkConnectivity(this)) {
            progressLayout.visibility = View.GONE
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonRequest = object :
                JsonObjectRequest(Method.GET, url + restaurant_id, null, Response.Listener {
                    try {
                        val response = it.getJSONObject("data")
                        val success = response.getBoolean("success")
                        if (success) {
                            val data = response.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val menuJsonObject = data.getJSONObject(i)
                                val menu = Details(
                                    menuJsonObject.getString("id"),
                                    menuJsonObject.getString("name"),
                                    menuJsonObject.getString("cost_for_one"),
                                    menuJsonObject.getString("restaurant_id")
                                )
                                DishList.add(menu)

                                recyclerAdapter = DetailsRecyclerAdapter(this, DishList, object :
                                    DetailsRecyclerAdapter.OnSelectListenerAdap {
                                    override fun onAddClickListener(foodItems: Details) {
                                        val foodEntity = DishEntity(
                                            foodItems.id,
                                            foodItems.name,
                                            foodItems.CostForOne,
                                            foodItems.ResId
                                        )
                                        orderList.add(foodItems)
                                        val async =
                                            DishDB(this@RestaurantDetails, foodEntity, 1).execute()
                                        val ans = async.get()
                                        if (!ans) {
                                            Toast.makeText(
                                                this@RestaurantDetails,
                                                "Some Error Occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@RestaurantDetails,
                                                "Item added to Cart",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        if (!orderList.isEmpty()) {
                                            btnCart.visibility = View.VISIBLE
                                        }
                                    }

                                    override fun onRemoveclickListener(foodItems: Details) {
                                        val foodEntity = DishEntity(
                                            foodItems.id,
                                            foodItems.name,
                                            foodItems.CostForOne,
                                            foodItems.ResId
                                        )
                                        orderList.remove(foodItems)
                                        val async =
                                            DishDB(this@RestaurantDetails, foodEntity, 2).execute()
                                        val ans = async.get()
                                        if (!ans) {
                                            Toast.makeText(
                                                this@RestaurantDetails,
                                                "Some Error Occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@RestaurantDetails,
                                                "Item removed to Cart",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        if (orderList.isEmpty()) {
                                            btnCart.visibility = View.GONE
                                        }
                                    }
                                }
                                )
                                DetailsRecycler.adapter = recyclerAdapter
                                DetailsRecycler.layoutManager = layoutManager
                            }
                        } else {
                            val dialog = AlertDialog.Builder(this)
                            dialog.setTitle("!!!!!")
                            dialog.setMessage("Error connecting to the internet")
                            dialog.setIcon(R.drawable.ic_error)
                            dialog.setPositiveButton("OK") { text, Listener ->
                            }
                            dialog.create()
                            dialog.show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this,
                            "Something went wrong Json Exception!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        this,
                        "Something went wrong Response Listener!!",
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
            queue.add(jsonRequest)
        } else {
            progressLayout.visibility = View.GONE
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Internet error")
            dialog.setMessage("Internet connection not found!!!")
            dialog.setPositiveButton("Open Settings") { text, Listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit App") { text, Listener ->
                ActivityCompat.finishAffinity(this)
            }
        }
        btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    class DishDB(val context: Context, val dishEntity: DishEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.dishDao().insertDish(dishEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.dishDao().deleteDish(dishEntity)
                    db.close()
                    return true
                }
                3 -> {
                    val present: DishEntity? = db.dishDao().getDishByid((dishEntity.id).toString())
                    db.close()
                    return present != null
                }
            }
            return false
        }
    }

    class DishDBDelete(val context: Context) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, DishDatabase::class.java, "dish-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            db.dishDao().deleteAllContents()
            db.close()
            return true
        }
    }

    override fun onBackPressed() {
        val async = DishDBDelete(applicationContext).execute()
        val ans = async.get()
        if (ans) {
            orderList.clear()
            super.onBackPressed()
        } else {
            Toast.makeText(this@RestaurantDetails, "Please try again!!!", Toast.LENGTH_SHORT).show()
        }
    }

    class FavouriteDB(val context: Context, val favouriteEntity: FavouriteEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, FavouriteDatabase::class.java, "fav-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.favouriteDao().insertRes(favouriteEntity)
                    db.close()
                    return true
                }
                2 -> {
                    db.favouriteDao().deleteRes(favouriteEntity)
                    db.close()
                    return true
                }
                3 -> {
                    val Res: FavouriteEntity? = db.favouriteDao().isPresent(favouriteEntity.res_id)
                    db.close()
                    return Res != null
                }
            }
            return false
        }
    }
}