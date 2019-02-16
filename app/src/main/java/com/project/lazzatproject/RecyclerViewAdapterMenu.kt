package com.project.lazzatproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewAdapterMenu(private var context: Context, private var MenuItemList: List<MenuItem>) : RecyclerView.Adapter<RecyclerViewAdapterMenu.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.menu_view_item, parent, false)


        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = MenuItemList[position].name
        holder.category.text = MenuItemList[position].category
        holder.price.text= MenuItemList[position].price.toString()



    }

    override fun getItemCount(): Int {
        return MenuItemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var name: TextView = itemView.findViewById<View>(R.id.itemname) as TextView
        internal var price: TextView = itemView.findViewById<View>(R.id.itemprice) as TextView
        internal var category: TextView = itemView.findViewById<View>(R.id.itemcategory) as TextView


    }
}
