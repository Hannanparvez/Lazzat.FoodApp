package com.project.lazzatproject


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlacePicker


class SettingsFragment : Fragment(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val PLACE_PICKER_REQUEST = 1
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        mGoogleApiClient = GoogleApiClient.Builder(context!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

//        //OnClickListener
        val viq = inflater.inflate(R.layout.fragment_settings, container, false)
//        val pick = viq.findViewById(R.id.btPlacePicker) as Button
        val test=viq.findViewById(R.id.test) as Button
        test.setOnClickListener {
            Toast.makeText(context,"test",Toast.LENGTH_SHORT).show()

        }
        test.setOnClickListener{
            Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show()
            if (checkGPSEnabled()) {
                val builder = PlacePicker.IntentBuilder()
                startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
            }

        }


        return inflater.inflate(R.layout.fragment_settings, container, false)


    }
    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun showAlert() {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Enable Location")
                .setMessage("Locations Settings is set to 'Off'.\nEnable Location to use this app")
                .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }

}
