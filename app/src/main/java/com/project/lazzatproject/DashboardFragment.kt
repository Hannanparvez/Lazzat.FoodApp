package com.project.lazzatproject

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import android.app.AlertDialog


class DashboardFragment : Fragment() {
    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var listCont: MutableList<MenuItem>
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        v = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = v.findViewById<View>(R.id.recycler_view_menu) as RecyclerView
        val viewAdapter = RecyclerViewAdapterMenu(this.context!!,listCont)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = viewAdapter
        return v

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loading = AlertDialog.Builder(activity)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.showprogress)
        val dialog = loading.create()
        dialog.show()

        listCont = ArrayList()
//        listCont.add(MenuItem("My Place", 100,"ih"))
//        listCont.add(MenuItem("My Place", 100,"ih"))


        mAuth = FirebaseAuth.getInstance()
        val currentuser=mAuth!!.currentUser
        val menuref = myRef.child("Users").child(currentuser!!.uid).child("menu")
        menuref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {




                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (snap in dataSnapshot.children) {
                    if(snap.hasChildren()){
                        for (sn in snap.children){
                            Log.d(TAG, snap.key.toString()+" : "+sn.key.toString()+" costs "+sn.value.toString())
                            var pric=sn.value.toString()
                            listCont.add(MenuItem(sn.key.toString(),pric.toInt(),snap.key.toString()))
                        }

//                        Toast.makeText(context,snap.key.toString()+"has children",Toast.LENGTH_SHORT).show()
                    }
                }
                Log.d(TAG, listCont.size.toString())
                if (isVisible){

                    val viewAdapter = RecyclerViewAdapterMenu(context!!,listCont)
                    recyclerView.adapter = viewAdapter
                }

                dialog.dismiss()


                // Sign in success, update UI with the signed-in user's information

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })




    }
}