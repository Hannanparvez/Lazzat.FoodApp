package com.project.lazzatproject


import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment





class HomeFragment : Fragment(),View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        var view = inflater.inflate(R.layout.fragment_home, container, false)


        var button = view.findViewById(R.id.fab_add) as ImageButton
        button.setOnClickListener(this)
        return view


    }
    override fun onClick(v: View) {

        val alert = AlertDialog.Builder(context)
        var category_name: EditText?=null
// Builder
        with (alert) {
            setTitle("Add category")
            setMessage("Name of the category")
// Add any  input field here
            category_name=EditText(context)
            category_name!!.hint="Category name"
            category_name!!.inputType = InputType.TYPE_CLASS_TEXT
            setPositiveButton("OK") { dialog, whichButton ->
                var Category = category_name!!.text.toString()
                if (Category != ""){
                    Toast.makeText(context, Category + " category created", Toast.LENGTH_SHORT).show()
                }
                //showMessage("display the game score or anything!")
                dialog.dismiss()

            }
            setNegativeButton("NO") {
                dialog, whichButton ->

                //showMessage("Close the game or anything!")
                dialog.dismiss()
            }
        }
// Dialog
        val dialog = alert.create()
        dialog.setView(category_name)
        dialog.show()

    }





}