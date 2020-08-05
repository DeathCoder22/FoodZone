package com.atul.foodzone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atul.foodzone.R
import com.atul.foodzone.model.Details

class CartAdapter(val context: Context,val itemList : ArrayList<Details>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_single_use,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val CartItem = itemList[position]
        holder.txtDishname.text = CartItem.name
        val cost = "Rs. ${CartItem.CostForOne}"
        holder.txtDishCost.text = cost
    }
    class CartViewHolder(view : View):RecyclerView.ViewHolder(view){
        val txtDishname : TextView = view.findViewById(R.id.txtDishName)
        val txtDishCost : TextView = view.findViewById(R.id.txtDishCost)
    }
}