package com.project.lazzatproject


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class UserHomeFragment : Fragment() {

    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var listCont: MutableList<Restaurant>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_user_home, container, false)
        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        val viewAdapter = RecycleViewAdapter(this.context!!, listCont)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = viewAdapter
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        listCont = ArrayList()
        listCont.add(Restaurant("My Place", "Food!!!", R.drawable.fifth))

        listCont.add(Restaurant("Lazzat Cafe", "Anything Available Here!!",R.drawable.first ))

        listCont.add(Restaurant("Hotel Pine", "Anything Available Here!!",R.drawable.second ))

        listCont.add(Restaurant("Smoke 'n Joes", "All the varieties of pizzaa!!",R.drawable.twelveth ))


        listCont.add(Restaurant("hotel Khyber", "Whole Kashmiri Cusiene!!",R.drawable.thirteenth ))


        listCont.add(Restaurant("Erina", "Ice creams and milk shakes!!",R.drawable.fourtheenth ))


        listCont.add(Restaurant("Hotel Madina", "Tea and Cake!!",R.drawable.fifteenth))



        listCont.add(Restaurant("Smoke 'n Joes", "All the varieties of pizzaa!!",R.drawable.twelveth ))


        listCont.add(Restaurant("hotel Khyber", "Whole Kashmiri Cusiene!!",R.drawable.thirteenth ))


        listCont.add(Restaurant("Erina", "Ice creams and milk shakes!!",R.drawable.fourtheenth ))


        listCont.add(Restaurant("Hotel Madina", "Tea and Cake!!",R.drawable.fifteenth))


    }

}
