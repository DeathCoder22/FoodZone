package com.atul.foodzone.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atul.foodzone.R
import com.atul.foodzone.activity.RestaurantDetails
import com.atul.foodzone.model.Restaurants
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context : Context,val itemList : ArrayList<Restaurants>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_single_use,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = itemList[position]
        holder.txtResName.text = item.name
        holder.txtRating.text = item.rating
        holder.txtCostforOne.text = item.CostForOne
        Picasso.get().load(item.imageUrl).error(R.drawable.food_drawer).into(holder.imgResImage)

        holder.llcontent.setOnClickListener {
            val intent = Intent(context,RestaurantDetails::class.java)
            intent.putExtra("id",item.id)
            intent.putExtra("resname",holder.txtResName.text.toString())
            intent.putExtra("resrating",holder.txtRating.text.toString())
            intent.putExtra("rescost",holder.txtCostforOne.text.toString())
            intent.putExtra("imageurl",item.imageUrl)
            context.startActivity(intent)
        }
    }

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val llcontent : LinearLayout = view.findViewById(R.id.llcontent)
        val imgResImage : ImageView = view.findViewById(R.id.imgResImage)
        val txtResName : TextView = view.findViewById(R.id.txtResName)
        val txtCostforOne : TextView = view.findViewById(R.id.txtCostForOne)
        val txtRating : TextView = view.findViewById(R.id.txtRating)
    }
}