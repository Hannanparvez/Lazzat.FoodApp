package com.project.lazzatproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OfferRecycleViewAdapter(private var context: Context, private var OfferList: List<Offer>) : RecyclerView.Adapter<OfferRecycleViewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.offercard, parent, false)


        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textt.text = OfferList[position].offertext!!.capitalize()

    }

    override fun getItemCount(): Int {
        return OfferList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textt: TextView = itemView.findViewById<View>(R.id.card_text) as TextView


    }
}
