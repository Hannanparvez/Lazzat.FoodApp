package com.project.lazzatproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast






//implement the interface OnNavigationItemSelectedListener in your activity class
class Owner_dashboard : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)
        setSupportActionBar(findViewById(R.id.toolbar2))

        //loading the default fragment
        loadFragment(HomeFragment())

        //getting bottom navigation view and attaching the listener

        val navigation = this.findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ownertop, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out ->{
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            // user is now signed out
                            startActivity(Intent(applicationContext, signin::class.java))


                            finish()

                        }
            }

        }
        return true
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.navigation_home -> fragment = HomeFragment()

            R.id.navigation_dashboard -> fragment = DashboardFragment()

            R.id.navigation_notifications -> fragment = NotificationsFragment()

            R.id.navigation_profile -> fragment = ProfileFragment()

            R.id.sign_out -> {
                if (item.itemId == R.id.sign_out) {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener {
                                // user is now signed out
                                startActivity(Intent(applicationContext, signin::class.java))
                                finish()

                            }
                }
            }
        }

        return loadFragment(fragment)
    }




    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            return true
        }
        return false
    }


}