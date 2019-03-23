package com.project.lazzatproject


import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.internal.db
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap


@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
open class UserHomeFragment : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null


    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var listCont: MutableList<Restaurant>


    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null

    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var locationManager: LocationManager? = null


    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private val isLocationEnabled: Boolean
        get() {
            locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance()

        v = inflater.inflate(R.layout.fragment_user_home, container, false)
        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        val viewAdapter = RecycleViewAdapter(this.context!!, listCont)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = viewAdapter

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        Log.d("gggg", "uooo")
        checkLocation()
        Log.d(ContentValues.TAG, "inside onCreate(): ")

        listCont = ArrayList()
        Log.d(ContentValues.TAG, "above myPlace: ")

        loadPost()


    }

    private fun loadPost() {
        val loading = AlertDialog.Builder(activity)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.fetchingrestaurents)
        val loadingdialog = loading.create()
        loadingdialog.show()


        myRef.child("Users").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val hashMap: HashMap<String, Double> = HashMap()
//                hashMap.clear()
                try {
                    Log.d(ContentValues.TAG, "inside onDataChange(): ")
                    val td = p0.value as HashMap<*, *>
                    for (key in td.keys) {

                        Log.d(ContentValues.TAG, "inside for loop: ")

                        val post = td[key] as HashMap<*, *>
                        if (post["type"] as String == "owner") {

                            val a = post["location"] as HashMap<*, *>
                            val owner_lat = a["latitude"]
                            val owner_lon = a["longitude"]
                            val owner_name = post["name"]


                            val dist = distance(owner_lat as Double, owner_lon as Double, lat, lon)

                            Log.d(TAG, "Owner $owner_name has location ($owner_lat,$owner_lon) with distance $dist ")

//                            listCont.add(Restaurant(post["name"] as String, post["shop_description"] as String,post["profile_pic"] as String,post["email"] as String))

                            hashMap[key.toString()] = dist.toDouble()

                            Log.d(ContentValues.TAG, "inside if(): ")
//                            listCont.add(Restaurant(post["name"] as String, post["shop_description"] as String,post["profile_pic"] as String,post["email"] as String,key as String))

                        }

                    }
                    val result = hashMap.toList().sortedBy { (_, value) -> value }.toMap()

                    for (key in result.keys) {
                        Log.d(TAG, "****INSIDE FREAKING FOR LOOP****")
                        Log.d(TAG, "$key = ${hashMap[key]}")
                        val post1 = td[key] as HashMap<*, *>
                        listCont.add(Restaurant(post1["name"] as String, post1["shop_description"] as String, post1["profile_pic"] as String, post1["email"] as String, key))

                    }
                    Log.d(TAG, "size is ${hashMap.size}")
                } catch (ex: Exception) {
                }

                if (isVisible) {

                    val viewAdapter = RecycleViewAdapter(context!!, listCont)
                    recyclerView.adapter = viewAdapter
                    loadingdialog.dismiss()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    123)

            ActivityCompat.requestPermissions(this.activity!!,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    123)
        }

        startLocationUpdates()

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(context, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i(TAG, "Connection Suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i(TAG, "Connection failed. Error: " + p0.errorCode)
    }

    override fun onLocationChanged(location: Location?) {
        lat = location!!.latitude
        lon = location.longitude
        val msg = "Updated Location: " +
                java.lang.Double.toString(lat) + "," +
                java.lang.Double.toString(lon)
        Log.d(TAG, msg)
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled)
            showAlert()
        return isLocationEnabled
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
                .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }


    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }



    private fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)
        Log.d("reque", "--->>>>")
    }

    fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians((lat2 - lat1))
        val dLng = Math.toRadians((lng2 - lng1))
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return ((earthRadius * c) / 1000).toFloat()
    }


}