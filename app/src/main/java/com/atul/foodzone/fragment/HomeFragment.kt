package com.atul.foodzone.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.atul.foodzone.R
import com.atul.foodzone.adapter.HomeRecyclerAdapter
import com.atul.foodzone.model.Restaurants
import com.atul.foodzone.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout : RelativeLayout
    lateinit var recyclerHome : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    var previousSortIcon : MenuItem ?= null
    var ResInfoList = arrayListOf<Restaurants>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var CostComparer = Comparator<Restaurants>{Res1,Res2->
        Res1.CostForOne.compareTo(Res2.CostForOne,true)
    }
    var ratingComparer = Comparator<Restaurants>{Res1,Res2->
        Res1.rating.compareTo(Res2.rating,true)
    }
    var nameComparer = Comparator<Restaurants>{Res1,Res2->
        Res1.name.compareTo(Res2.name,true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        recyclerHome = view.findViewById(R.id.homeRecycler)

        progressLayout.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity as Context)

        setHasOptionsMenu(true)
        if(ConnectionManager().checkConnectivity(activity as Context)){
            progressLayout.visibility = View.GONE
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

            val jsonRequest = object : JsonObjectRequest(Method.GET,url,null, Response.Listener {
                try{
                    val response = it.getJSONObject("data")
                    val success = response.getBoolean("success")
                    if(success){
                        val data = response.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val ResJsonObject = data.getJSONObject(i)
                            val Res = Restaurants(
                                ResJsonObject.getString("id"),
                                ResJsonObject.getString("name"),
                                ResJsonObject.getString("rating"),
                                ResJsonObject.getString("cost_for_one"),
                                ResJsonObject.getString("image_url")
                            )
                            ResInfoList.add(Res)
                            Collections.sort(ResInfoList,nameComparer)
                            ResInfoList.reverse()
                            recyclerAdapter = HomeRecyclerAdapter(activity as Context,ResInfoList)
                            recyclerHome.adapter = recyclerAdapter
                            recyclerHome.layoutManager = layoutManager
                        }
                    }else{
                        Toast.makeText(activity as Context,"Something went wrong!!",Toast.LENGTH_SHORT).show()
                    }
                }catch (e : JSONException){
                    Toast.makeText(activity as Context,"Something went wrong!!",Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(activity as Context,"Something went wrong!!",Toast.LENGTH_SHORT).show()
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
            progressLayout.visibility = View.GONE
            val dialog = AlertDialog.Builder(activity as Activity)
            dialog.setTitle("Internet error")
            dialog.setMessage("Internet connection not found!!!")
            dialog.setPositiveButton("Open Settings"){text,Listener->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Close App"){text,Listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_home,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(previousSortIcon!=null){
            previousSortIcon?.isChecked = false
            previousSortIcon = item
        }
        val id = item?.itemId
        item.isCheckable = true
        item.isChecked = true
        if (id == R.id.defaultSort){
            item.isChecked = false
        }
        when(id){
            R.id.sortCostAscending->{
                Collections.sort(ResInfoList, CostComparer)
            }
            R.id.sortCostDescending->{
                Collections.sort(ResInfoList,CostComparer)
                ResInfoList.reverse()
            }
            R.id.sortRating->{
                Collections.sort(ResInfoList,ratingComparer)
                ResInfoList.reverse()
            }
            R.id.sortName->{
               Collections.sort(ResInfoList,nameComparer)
            }
            R.id.defaultSort->{
                Collections.sort(ResInfoList,nameComparer)
                ResInfoList.reverse()
            }
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}