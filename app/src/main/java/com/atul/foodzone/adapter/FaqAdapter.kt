package com.atul.foodzone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atul.foodzone.R
import com.atul.foodzone.model.QuesAns


class FaqAdapter(val context: Context,val itemList : ArrayList<QuesAns>):RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    class FaqViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ques = view.findViewById<TextView>(R.id.ques)
        val ans = view.findViewById<TextView>(R.id.ans)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.ques_ans_single_use,parent,false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val item = itemList[position]
        holder.ques.text = item.ques
        holder.ans.text = item.ans
    }
}