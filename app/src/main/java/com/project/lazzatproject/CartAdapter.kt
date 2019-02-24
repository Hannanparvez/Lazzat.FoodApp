package com.project.lazzatproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CartAdapter(private var context: Context, private var cartList: List<CartModel>) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)


        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = cartList[position].itemName
        holder.size.text = cartList[position].size
        holder.rate.text = cartList[position].rate




    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name: TextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        internal var size: TextView = itemView.findViewById<View>(R.id.tv_size) as TextView
        internal var rate: TextView = itemView.findViewById<View>(R.id.tv_rate) as TextView

    }
}
