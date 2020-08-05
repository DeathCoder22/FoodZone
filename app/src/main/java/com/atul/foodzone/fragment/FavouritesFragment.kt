package com.atul.foodzone.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.atul.foodzone.R
import com.atul.foodzone.adapter.FavouriteRecyclerAdapter
import com.atul.foodzone.database.FavouriteDatabase
import com.atul.foodzone.database.FavouriteEntity
import com.atul.foodzone.model.Restaurants

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {
    lateinit var FavouriteRecycler : RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    var ResList = arrayListOf<Restaurants>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        FavouriteRecycler = view.findViewById(R.id.FavouriteRecycler)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(activity as Context)

        progressLayout.visibility = View.VISIBLE

        val dbList = GetAllFav(activity as Context).execute().get()
        for(i in 0 until dbList.size){
            val res = Restaurants(
                dbList[i].res_id,
                dbList[i].resName,
                dbList[i].resRating,
                dbList[i].resCostForOne,
                dbList[i].ImageUrl
            )
            ResList.add(res)
        }
        recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,ResList)
        FavouriteRecycler.adapter = recyclerAdapter
        FavouriteRecycler.layoutManager = layoutManager

        progressLayout.visibility = View.GONE
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavouritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    class FavouriteDB(val context: Context,val favouriteEntity: FavouriteEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
        val db = Room.databaseBuilder(context,FavouriteDatabase::class.java,"fav-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    db.favouriteDao().insertRes(favouriteEntity)
                    db.close()
                    return true
                }
                2->{
                    db.favouriteDao().deleteRes(favouriteEntity)
                    db.close()
                    return true
                }
                3->{
                    val Res:FavouriteEntity ?= db.favouriteDao().isPresent(favouriteEntity.res_id)
                    db.close()
                    return Res!=null
                }
            }
            return false
        }

    }
    class GetAllFav(val context: Context):AsyncTask<Void,Void,List<FavouriteEntity>>(){
        val db = Room.databaseBuilder(context,FavouriteDatabase::class.java,"fav-db").build()
        override fun doInBackground(vararg p0: Void?): List<FavouriteEntity> {
            return db.favouriteDao().getAllRes()
        }

    }
}