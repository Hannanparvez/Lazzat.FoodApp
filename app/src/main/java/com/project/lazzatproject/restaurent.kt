package com.project.lazzatproject

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurent.*


class restaurent : Activity() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    internal var expandableListView: ExpandableListView? = null
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()
            var extras = getIntent().getExtras();
            val menuref = myRef.child("Users").child(extras.getString("UID")).child("menu")
            menuref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (snap in dataSnapshot.children) {
                        val temp = ArrayList<String>()
                        if (snap.hasChildren()){
                            Log.d("test",snap.key.toString()+"has children")
                            for (snaap in snap.children){
                                temp.add(snaap.key.toString()+"$"+snaap.value.toString())

                            }
                        }

                        listData[snap.key.toString()]=temp
                        Log.d("hi",snap.key.toString())

                    }
                    Log.d("hi",listData.size.toString())

                        titleList = ArrayList(listData.keys)
                        adapter = CustomMenuExpandableListAdapter(applicationContext, titleList as ArrayList<String>, listData)
                        expandableListView!!.setAdapter(adapter)

//                        expandableListView!!.setOnGroupExpandListener {
//                            groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show()
//                        }
//
//                        expandableListView!!.setOnGroupCollapseListener {
//                            groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show()
//                        }

                     // Sign in success, update UI with the signed-in user's information

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })

            Log.d("hiojj",extras.getString("UID"))

            return listData
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurent)

            var extras = getIntent().getExtras();

                Log.d("hannn",extras.getString("STRING_I_NEED"))

        myRef.child("Users").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    Log.d(ContentValues.TAG, "inside onDataChange(): ")

                    val td = p0.value as java.util.HashMap<*, *>

                    for(key in td.keys){
                        Log.d(ContentValues.TAG, "inside for loop: ")

                        val post = td[key] as java.util.HashMap<*, *>
                        if(post["email"] as String == extras.getString("STRING_I_NEED")) {
                            Picasso.get().load(post["profile_pic"] as String ).into(imageView4)
                            val x=post["shop_name"] as String
                            val y=post["shop_description"] as String
                            val z=post["location"] as java.util.HashMap<*, *>
                            val k=z["address"] as String

                            textView11.text=x.capitalize()
                            textView12.text=y.capitalize()
                            button5.setOnClickListener {
//                                Toast.makeText(applicationContext, "Clicked: " , Toast.LENGTH_SHORT).show()
                                val intent = Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=34.154492999999995,74.64786269999999&daddr="+z["latitude"].toString()+","+z["longitude"].toString()))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                                startActivity(intent)

                            }
//                            Log.d("hijj",key as String)

//                            menuref = myRef.child("Users").child(key).child("menu")

                        }
                    }

                }catch (ex:Exception){}
//                if (isVisible){
//
//                    val viewAdapter = RecycleViewAdapter(context!!,listCont)
//                    recyclerView.adapter = viewAdapter
//                    loadingdialog.dismiss()
//                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
            expandableListView = findViewById(R.id.restaurantmenu)

            if (expandableListView != null) {

                val listData = data
                titleList = ArrayList(listData.keys)
                adapter = CustomMenuExpandableListAdapter(this, titleList as ArrayList<String>, listData!!)
                expandableListView!!.setAdapter(adapter)

//                expandableListView!!.setOnGroupExpandListener { groupPosition -> Toast.makeText(applicationContext, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show() }

//                expandableListView!!.setOnGroupCollapseListener { groupPosition -> Toast.makeText(applicationContext, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show() }

                expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//                    Toast.makeText(applicationContext, "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

    }

