
package com.project.lazzatproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CartFragment : Fragment() {

    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var listCart: MutableList<CartModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = v.findViewById<View>(R.id.recycler_cart) as RecyclerView
        val viewAdapter = CartAdapter(this.context!!, listCart)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = viewAdapter
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     listCart  = ArrayList()
    listCart.add(CartModel("hdbjhbds", "big", "1211"))
        listCart.add(CartModel("hdbjhbds", "big", "1211"))
        listCart.add(CartModel("hdbjhbds", "big", "1211"))
        listCart.add(CartModel("hdbjhbds", "big", "1211"))

    }

}
