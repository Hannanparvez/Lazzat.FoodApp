package com.project.lazzatproject

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_owner.*


class DashboardFragment : Fragment() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    internal var expandableListView: ExpandableListView? = null
    internal var adapter: CustomExpandableListAdapter? = null
    internal var titleList: List<String> ? = null

    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()

            val redmiMobiles = ArrayList<String>()
            redmiMobiles.add("Redmi Y2")
            redmiMobiles.add("Redmi S2")
            redmiMobiles.add("Redmi Note 5 Pro")
            redmiMobiles.add("Redmi Note 5")
            redmiMobiles.add("Redmi 5 Plus")
            redmiMobiles.add("Redmi Y1")
            redmiMobiles.add("Redmi 3S Plus")

            val micromaxMobiles = ArrayList<String>()
            micromaxMobiles.add("Micromax Bharat Go")
            micromaxMobiles.add("Micromax Bharat 5 Pro")
            micromaxMobiles.add("Micromax Bharat 5")
            micromaxMobiles.add("Micromax Canvas 1")
            micromaxMobiles.add("Micromax Dual 5")

            val appleMobiles = ArrayList<String>()
            appleMobiles.add("iPhone 8")
            appleMobiles.add("iPhone 8 Plus")
            appleMobiles.add("iPhone X")
            appleMobiles.add("iPhone 7 Plus")
            appleMobiles.add("iPhone 7")
            appleMobiles.add("iPhone 6 Plus")

            val samsungMobiles = ArrayList<String>()
            samsungMobiles.add("Samsung Galaxy S9+")
            samsungMobiles.add("Samsung Galaxy Note 7")
            samsungMobiles.add("Samsung Galaxy Note 5 Dual")
            samsungMobiles.add("Samsung Galaxy S8")
            samsungMobiles.add("Samsung Galaxy A8")
            samsungMobiles.add("Samsung Galaxy Note 4")
            val currentuser=mAuth!!.currentUser
            val menuref = myRef.child("Users").child(currentuser!!.uid).child("menu")
            menuref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (snap in dataSnapshot.children) {
                        val temp = ArrayList<String>()
                        if (snap.hasChildren()){
                            Log.d("test",snap.key.toString()+"has children")
                            for (snaap in snap.children){
                                temp.add(snaap.key.toString()+"  RS: "+snaap.value.toString())

                            }
                        }

                        listData[snap.key.toString()]=temp
                        Log.d("hi",snap.key.toString())

                    }
                    Log.d("hi",listData.size.toString())
                    if (isVisible) {
                        titleList = ArrayList(listData.keys)
                        adapter = CustomExpandableListAdapter(context!!, titleList as ArrayList<String>, listData)
                        expandableListView!!.setAdapter(adapter)

                        expandableListView!!.setOnGroupExpandListener { groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show() }

                        expandableListView!!.setOnGroupCollapseListener { groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show() }

                        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                            Toast.makeText(context, "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                            false
                        }
                    }








                    // Sign in success, update UI with the signed-in user's information

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })



//            listData["Redmi"] = redmiMobiles
//            listData["Micromax"] = micromaxMobiles
//            listData["Apple"] = appleMobiles
//            listData["Samsung"] = samsungMobiles
            Log.d("soid",listData.size.toString())
//            Toast.makeText(context,listData.size.toString(), Toast.LENGTH_SHORT).show()
            return listData
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
                setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)



        expandableListView =view!!.findViewById(R.id.expandableListView)
        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(context!!, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)

            expandableListView!!.setOnGroupExpandListener { groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show() }

            expandableListView!!.setOnGroupCollapseListener { groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show() }

            expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Toast.makeText(context, "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }
        }
        return view
    }
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ownertop,menu)
        super.onCreateOptionsMenu(menu, inflater)
        var refresh=menu.findItem(R.id.refresh)
        refresh.setVisible(true)
        var signout=menu.findItem(R.id.sign_out1)
        signout.setVisible(false)
    }

}