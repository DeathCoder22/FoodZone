package com.atul.foodzone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.atul.foodzone.R
import com.atul.foodzone.model.Details

class DetailsRecyclerAdapter(val context: Context,val itemList: ArrayList<Details>,val onSelectListenerAdap: OnSelectListenerAdap):RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.details_restaurant_single_use,parent,false)
        return DetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  itemList.size
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val ResDetail = itemList[position]
        holder.sno.text = (position+1).toString()
        holder.txtnameRes.text =  ResDetail.name
        holder.txtCostRes.text = ResDetail.CostForOne

        holder.btnAddorRemove.text = context.getString(R.string.add)
        val addCol = ContextCompat.getColor(context,R.color.add)
        holder.btnAddorRemove.setBackgroundColor(addCol)

        holder.btnAddorRemove.setOnClickListener {
            if(holder.btnAddorRemove.text == "Add"){
                holder.btnAddorRemove.text = context.getString(R.string.remove)
                val remCol = ContextCompat.getColor(context,R.color.remove)
                holder.btnAddorRemove.setBackgroundColor(remCol)
                onSelectListenerAdap.onAddClickListener(ResDetail)
            }else{
                holder.btnAddorRemove.text = context.getString(R.string.add)
                val addColor = ContextCompat.getColor(context,R.color.add)
                holder.btnAddorRemove.setBackgroundColor(addColor)
                onSelectListenerAdap.onRemoveclickListener(ResDetail)
            }
        }
    }
    class DetailsViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtnameRes : TextView = view.findViewById(R.id.txtNameRes)
        val txtCostRes : TextView = view.findViewById(R.id.txtCostRes)
        val btnAddorRemove : Button = view.findViewById(R.id.btnAddorRemove)
        val sno: TextView = view.findViewById(R.id.txtSNo)
    }
    interface OnSelectListenerAdap {
        fun onAddClickListener(foodItems: Details)
        fun onRemoveclickListener(foodItems: Details)
    }

}