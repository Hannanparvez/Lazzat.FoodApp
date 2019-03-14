package com.project.lazzatproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.lazzatproject.R.id.*


//implement the interface OnNavigationItemSelectedListener in your activity class
class Owner_dashboard : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    var sharedpref: SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {

        sharedpref=this.getSharedPreferences("actype", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)

        setSupportActionBar(findViewById(toolbar2))

        //loading the default fragment
        loadFragment(HomeFragment())

        //getting bottom navigation view and attaching the listener

        val navigation = this.findViewById<BottomNavigationView>(navigation)
        navigation.setOnNavigationItemSelectedListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ownertop, menu)
//





        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            sign_out1 ->{
                var save = sharedpref!!.edit()
                save.putString("type", "")
                save.apply()
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            // user is now signed out
                            startActivity(Intent(applicationContext, signin::class.java))


                            finish()

                        }
            }
            refresh ->{
                var ft:FragmentTransaction= supportFragmentManager.beginTransaction();
                ft.replace(fragment_container, DashboardFragment() as Fragment);
                ft.commit()
                Log.d("ref","refresh")



            }
            addd -> {
                var fragment: Fragment? = null
                fragment=HomeFragment()

                loadFragment(fragment)
            }




        }
        return true
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null

        when (item.itemId) {
            navigation_home -> fragment = HomeFragment()

            navigation_dashboard -> fragment = DashboardFragment()

            navigation_notifications -> fragment = NotificationsFragment()

            navigation_profile -> fragment = ProfileFragment()


 }

        return loadFragment(fragment)
    }




    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(fragment_container, fragment)
                    .commit()
            return true
        }
        return false
    }


}