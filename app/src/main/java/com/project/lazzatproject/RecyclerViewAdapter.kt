package com.project.lazzatproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView





class RecycleViewAdapter(private var context: Context, private var restaurantList: List<Restaurant>) : RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.restaurant_view_item, parent, false)


        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = restaurantList[position].name
        holder.spec.text = restaurantList[position].spec
        holder.imageView.setImageResource(restaurantList[position].photo)
        holder.itemView.setOnClickListener(View.OnClickListener {
              Toast.makeText(context,restaurantList[position].name, Toast.LENGTH_SHORT).show()
        })


    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name: TextView = itemView.findViewById<View>(R.id.restaurantname) as TextView
        internal var spec: TextView = itemView.findViewById<View>(R.id.speciality) as TextView
        internal var imageView: ImageView = itemView.findViewById<View>(R.id.imageView2) as ImageView


    }
}
