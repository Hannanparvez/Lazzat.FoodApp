package com.project.lazzatproject

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.google.android.libraries.places.internal.v
import de.hdodenhof.circleimageview.CircleImageView


class RecycleViewAdapter(private var context: Context, private var restaurantList: List<Restaurant>) : RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.restaurant_view_item, parent, false)


        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = restaurantList[position].name!!.capitalize()
        holder.spec.text = restaurantList[position].spec!!.capitalize()
//        holder.imageView.setImageResource(restaurantList[position].photo)
        holder.imageView.clipToOutline = true
        Log.d("han",restaurantList[position].photo)
        if (restaurantList[position].photo=="none"){
            Picasso.get().load(R.drawable.back).into(holder.imageView)
        }
        else {
            Picasso.get().load(restaurantList[position].photo).into(holder.imageView)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {

            val intento = Intent(it.context, restaurent::class.java)
            intento.putExtra("STRING_I_NEED", restaurantList[position].email)
            intento.putExtra("UID",restaurantList[position].uid)

            context.startActivity(intento)
//            Toast.makeText(context,restaurantList[position].name, Toast.LENGTH_SHORT).show()
        })


    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name: TextView = itemView.findViewById<View>(R.id.restaurantname) as TextView
        internal var spec: TextView = itemView.findViewById<View>(R.id.speciality) as TextView
        internal var imageView: CircleImageView= itemView.findViewById<View>(R.id.imageView2) as CircleImageView


    }
}
