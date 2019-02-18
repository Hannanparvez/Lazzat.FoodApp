package com.project.lazzatproject

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


class RecyclerViewAdapterMenu(private var context: Context, private var MenuItemList: List<MenuItem>) : RecyclerView.Adapter<RecyclerViewAdapterMenu.MyViewHolder>() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    private var mAuth: FirebaseAuth? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mAuth = FirebaseAuth.getInstance()
        val v: View = LayoutInflater.from(context).inflate(R.layout.menu_view_item, parent, false)


        return MyViewHolder(v)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = MenuItemList[position].name
        holder.category.text = MenuItemList[position].category
        holder.price.text= MenuItemList[position].price.toString()
        holder.itemView.setOnClickListener {
            val currentuser = mAuth!!.currentUser
            var productcategory=""
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.editmenuitem)
            dialog.setCancelable(true)

            // set the custom dialog components - text, image and button
            val spinner = dialog.findViewById(R.id.productcategory1) as Spinner
            val productname = dialog.findViewById(R.id.productname1) as EditText
            productname.setText(MenuItemList[position].name)
            val productprice = dialog.findViewById(R.id.productprice1) as EditText
            productprice.setText(MenuItemList[position].price.toString())


            val removeitem = dialog.findViewById(R.id.removeitem) as Button
            val edititem = dialog.findViewById(R.id.edititem) as Button

            var listofcategories = arrayListOf<String>("none")
            val menuref = myRef.child("Users").child(currentuser!!.uid).child("menu")
            menuref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (snap in dataSnapshot.children) {
                        listofcategories.add( snap.key.toString())
                    }


                    // Sign in success, update UI with the signed-in user's information

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value

                }
            })




            val adp = ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, listofcategories)
            spinner.adapter = adp
            Log.d("tag",getIndex(spinner, MenuItemList[position].category!!).toString())
            spinner.setSelection(getIndex(spinner, MenuItemList[position].category!!))
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // TODO Auto-generated method stub
                    productcategory= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // TODO Auto-generated method stub

                }
            }
            dialog.show()
            removeitem.setOnClickListener{
                menuref.child(productcategory).child(MenuItemList[position].name!!)
                        .removeValue()
                Toast.makeText(context,MenuItemList[position].name+" has been removed",Toast.LENGTH_SHORT).show()
                MenuItemList.drop(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,MenuItemList.size)
//
                dialog.setOnDismissListener {
                    Log.d("res","dis")

                }
                dialog.dismiss()



            }
            edititem.setOnClickListener{

                if (productcategory=="none"){
                    Toast.makeText(context,listofcategories.size.toString(),Toast.LENGTH_SHORT).show()


                }
                else if (listofcategories.size==1){
                    Toast.makeText(context,"add categories first",Toast.LENGTH_SHORT).show()


                }


                else if (productname.text.toString()==""){
                    Toast.makeText(context,"Select a product name",Toast.LENGTH_SHORT).show()


                }
                else if (productprice.text.toString()==""){
                    Toast.makeText(context,"Select a product price",Toast.LENGTH_SHORT).show()


                }
                else{
                    menuref.child(productcategory).child(MenuItemList[position].name!!)
                            .setValue(productprice.text.toString())

                    Toast.makeText(context,"Your product has been added",Toast.LENGTH_SHORT).show()

                    dialog.dismiss()


                }

            }


        }


    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
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
