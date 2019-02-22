package com.project.lazzatproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup.*


class signup : Activity() {
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    private lateinit var accounttype:String
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth=FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }
    override fun onStart() {
        super.onStart()
        updateUI()

    }

    fun busignup(view:View){
        if (acctype.checkedRadioButtonId == -1)
        {
            Toast.makeText(applicationContext,"select the account type", Toast.LENGTH_LONG).show()
            return
        }
        else
        {
            if (cus.isChecked){
                accounttype="customer"

            }
            if (own.isChecked){
                accounttype="owner"

            }
            // one of the radio buttons is checked
        }

          signuptofirebase(email.text.toString(),password.text.toString(),username.text.toString())
    }

    private fun signuptofirebase(email: String, password: String, name: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {

                        val currentuser = mAuth!!.currentUser
                        myRef.child("Users").child(currentuser!!.uid).child("email").setValue(email)
                        myRef.child("Users").child(currentuser.uid).child("name").setValue(name)
                        myRef.child("Users").child(currentuser.uid).child("profile_pic").setValue("true")
                        myRef.child("Users").child(currentuser.uid).child("type").setValue(accounttype)
                        if (accounttype=="owner"){
                            myRef.child("Users").child(currentuser.uid).child("menu").setValue("true")


                        }
                        // Sign in success, update UI with the signed-in user's information
                      Toast.makeText(applicationContext, "account created",
                                Toast.LENGTH_LONG).show()
                        updateUI()

                    } else {
                        // If sign in fails, display a message to the user.
//
                        Toast.makeText(applicationContext, "Authentication failed.",
                                Toast.LENGTH_LONG).show()
//                        updateUI(null)
                    }

                    // ...
                })
    }
    fun gotosignin(view: View){
        val intent=Intent(this,signin::class.java)
        startActivity(intent)
    }
    private fun updateUI(){
        val currentuser=mAuth!!.currentUser


        if (currentuser!=null){
            val mref=myRef.child("Users").child(currentuser.uid)
            mref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val typ = dataSnapshot.child("type").value as String

                    if (typ=="customer"){
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)

                    }
                    if (typ=="owner"){
                        val intent = Intent(applicationContext, Owner_dashboard::class.java)
                        startActivity(intent)


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }

    }

}
