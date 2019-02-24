package com.project.lazzatproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signin.*



class signin : AppCompatActivity() {
    var sharedpref:SharedPreferences?=null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signin)
        sharedpref=this.getSharedPreferences("actype",Context.MODE_PRIVATE)
    }

    override fun onStart() {

        super.onStart()
        updateUI()

    }



    fun bulogin(view:View){
        logintofirebase(email.text.toString(),password.text.toString())
//



    }


    fun logintofirebase(email:String,password:String){
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val currentuser=mAuth!!.currentUser
                        Log.d("h","task")
                        val mref=myRef.child("Users").child(currentuser!!.uid)
                        mref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                Log.d("hi","han")
                                val typ = dataSnapshot.child("type").value as String
                                var save = sharedpref!!.edit()
                                save.putString("type", typ)
                                save.apply()
                                updateUI()

                            }
                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })


                        }

                    else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(applicationContext, "Authentication failed.",
                                Toast.LENGTH_LONG).show()
//                        updateUI(null)
                    }

                    // ...
//

                }

    }

    fun updateUI(){
        Log.d("h","update")

        val currentuser=mAuth!!.currentUser
//
//
        if (currentuser!=null) {


            // This method is called once with the initial value and again
            // whenever data at this location is updated.
//                    val typ = dataSnapshot.child("type").value as String


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



    fun gotosignup(view: View) {
        val intent = Intent(applicationContext, signup::class.java)
        startActivity(intent)
    }


}

