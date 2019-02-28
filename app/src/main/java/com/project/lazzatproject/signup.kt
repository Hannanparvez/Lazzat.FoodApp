package com.project.lazzatproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
    var sharedpref: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpref=this.getSharedPreferences("actype", Context.MODE_PRIVATE)
        mAuth=FirebaseAuth.getInstance()

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
                            myRef.child("Users").child(currentuser.uid).child("location").setValue("none")


                        }
                        // Sign in success, update UI with the signed-in user's information
                        var save = sharedpref!!.edit()
                        save.putString("type", accounttype)
                        save.apply()
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
        finish()
    }
    private fun updateUI(){
        Log.d("hoi","hios")
        val currentuser=mAuth!!.currentUser


        if (currentuser!=null){
            var stype = sharedpref!!.getString("type", "")
            if (stype == "customer") {
                val intent = Intent(applicationContext, MainActivity::class.java)

                startActivity(intent)
                finish()
                return

            }
            if (stype == "owner") {

                val intent = Intent(applicationContext, Owner_dashboard::class.java)
                startActivity(intent)
                finish()
                return


            }


        }

    }

}
