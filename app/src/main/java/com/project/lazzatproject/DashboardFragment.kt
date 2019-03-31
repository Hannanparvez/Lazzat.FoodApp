package com.project.lazzatproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class DashboardFragment : Fragment() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    internal var expandableListView: ExpandableListView? = null
    internal var adapter: CustomExpandableListAdapter? = null
    internal var titleList: List<String> ? = null
    var loadingdialog:AlertDialog?=null

    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()
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
                                temp.add(snaap.key.toString().capitalize()+"$"+snaap.value.toString())

                            }
                        }

                        listData[snap.key.toString().capitalize()]=temp
                        Log.d("hi",snap.key.toString())

                    }
                    Log.d("hi",listData.size.toString())
                    if (isVisible) {
                        titleList = ArrayList(listData.keys)
                        adapter = CustomExpandableListAdapter(context!!, titleList as ArrayList<String>, listData)
                        expandableListView!!.setAdapter(adapter)

//                        expandableListView!!.setOnGroupExpandListener {
//                            groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show()
//                        }
//
//                        expandableListView!!.setOnGroupCollapseListener {
//                            groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show()
//                        }

                        loadingdialog!!.dismiss()
                    }  // Sign in success, update UI with the signed-in user's information

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
            return listData
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        val loading = AlertDialog.Builder(activity)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.showprogress)
        loadingdialog= loading.create()
        loadingdialog!!.show()
        super.onCreate(savedInstanceState)
                setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        activity!!.title = "Menu"
        val currentuser=mAuth!!.currentUser

        val menuref = myRef.child("Users").child(currentuser!!.uid).child("menu")





        expandableListView =view!!.findViewById(R.id.expandableListView)
        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(context!!, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)
//
//            expandableListView!!.setOnGroupExpandListener { groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show() }
//
//            expandableListView!!.setOnGroupCollapseListener { groupPosition -> Toast.makeText(context, (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show() }

            expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.editmenuitem)
                dialog.setCancelable(true)
                var sp=listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition).split("$")


                // set the custom dialog components - text, image and button
                val spinner = dialog.findViewById(R.id.productcategory1) as TextView
                val productname = dialog.findViewById(R.id.productname1) as EditText
                spinner.text="CATEGORY :"+(titleList as ArrayList<String>)[groupPosition]
                productname.setText(sp[0])
                val productprice = dialog.findViewById(R.id.productprice1) as EditText
                productprice.setText(sp[1])
                val removeitem = dialog.findViewById(R.id.removeitem) as Button
                val edititem = dialog.findViewById(R.id.edititem) as Button
                val editcancel=dialog.findViewById(R.id.canceledit) as Button
                dialog.show()
                editcancel.setOnClickListener{
                    dialog.dismiss()
                }
                removeitem.setOnClickListener{
                    menuref.child((titleList as ArrayList<String>)[groupPosition].decapitalize()).child(sp[0].decapitalize())
                            .removeValue()
                    dialog.dismiss()
                    var ft: FragmentTransaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fragment_container, DashboardFragment() as Fragment)
                    ft.commit()
//                                Toast.makeText(context,MenuItemList[position].name+" has been removed",Toast.LENGTH_SHORT).show()
                }
                edititem.setOnClickListener {
                    menuref.child((titleList as ArrayList<String>)[groupPosition].decapitalize()).child(sp[0].decapitalize())
                            .setValue(productprice.text.toString())

                    Toast.makeText(context,"Your product has been added",Toast.LENGTH_SHORT).show()
                    var ft: FragmentTransaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fragment_container, DashboardFragment() as Fragment);
                    ft.commit()

                    dialog.dismiss()

                }
//                Toast.makeText(context, "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(childPosition), Toast.LENGTH_SHORT).show()
                false
            }
        }
        return view
    }
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ownertop,menu)
        super.onCreateOptionsMenu(menu, inflater)
            val addd=menu.findItem(R.id.addd)
        var refresh=menu.findItem(R.id.refresh)
        refresh.setVisible(true)
            addd.setVisible(true)
        var signout=menu.findItem(R.id.sign_out1)
        signout.setVisible(false)
    }

}