package com.project.lazzatproject


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.schibstedspain.leku.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.sql.Timestamp
import java.util.*


class HomeFragment : Fragment(),GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{
    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    private var mStorageRef: StorageReference? = null
    private var currentuser: FirebaseUser? = null

    private var chooseofferpicdialog: Dialog? = null
    private var filePath: Uri? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        mAuth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        activity!!.title = "Home"
        mStorageRef = FirebaseStorage.getInstance().reference



        currentuser = mAuth!!.currentUser

        val addcategorys = view.findViewById(R.id.cardaddcat) as CardView

        addcategorys.setOnClickListener {
            val alert = AlertDialog.Builder(context)
            var category_name: EditText? = null
            with(alert) {
                setTitle("Add category")
                setMessage("Name of the category")
// Add any  input field here
                category_name = EditText(context)
                category_name!!.hint = "Category name"
                category_name!!.inputType = InputType.TYPE_CLASS_TEXT
                setNeutralButton("OK") { dialog, whichButton ->
                    var Category = category_name!!.text.toString()
                    if (Category != "") {
                        if (currentuser != null) {
                            myRef.child("Users").child(currentuser!!.uid).child("menu").child(Category).setValue("true")
                        }
                        Toast.makeText(context, Category + " category has been created", Toast.LENGTH_SHORT).show()

                    }
                    dialog.dismiss()

                }
                setNegativeButton("NO") { dialog, whichButton ->

                    //showMessage("Close the game or anything!")
                    dialog.dismiss()
                }
            }
// Dialog
            val dialog = alert.create()
            dialog.setView(category_name)
            dialog.show()
        }
        val addproducts = view.findViewById(R.id.cardaddpro) as CardView
        addproducts.setOnClickListener {


            var productcategory=""
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.addmenudailog)
            dialog.setCancelable(true)

            // set the custom dialog components - text, image and button
            val spinner = dialog.findViewById(R.id.productcategory) as Spinner
            val productname = dialog.findViewById(R.id.productname) as EditText
            val productprice = dialog.findViewById(R.id.productprice) as EditText


            val addproduct = dialog.findViewById(R.id.addproduct) as Button
            val canceladd = dialog.findViewById(R.id.canceladd) as Button

            var listofcategories = arrayListOf<String>("--Select a Category--")
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
            spinner.adapter = adp as SpinnerAdapter?
            spinner.setSelection(0)
            spinner.onItemSelectedListener = object : OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    // TODO Auto-generated method stub
                   productcategory= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // TODO Auto-generated method stub

                }
            }
            dialog.show()
            canceladd.setOnClickListener{
                dialog.dismiss()
            }
            addproduct.setOnClickListener{

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
                    menuref.child(productcategory).child(productname.text.toString())
                            .setValue(productprice.text.toString())

                    Toast.makeText(context,"Your product has been added",Toast.LENGTH_SHORT).show()

                    dialog.dismiss()


                }

            }


        }


        val addoffers =view.findViewById(R.id.cardoffers) as CardView
        addoffers.setOnClickListener{
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.addoffers)
            dialog.setCancelable(true)
            chooseofferpicdialog=dialog
            dialog.show()
            val cancel=dialog.findViewById(R.id.button3) as Button
            cancel.setOnClickListener {
                Log.d("can", Timestamp(System.currentTimeMillis()).toString())
                dialog.dismiss()
            }
            val initiate=dialog.findViewById(R.id.button4) as Button
            initiate.setOnClickListener {
                val tv1=dialog.findViewById(R.id.editText) as EditText
                val tv2=dialog.findViewById(R.id.textView10) as TextView

                uploadImage(tv1.text.toString(),tv2.text.toString())
                dialog.dismiss()


            }
            val choosepic=dialog.findViewById(R.id.button2) as Button
            choosepic.setOnClickListener {
                chooseImage()
            }

        }


//        //OnClickListener
//        val pick = viq.findViewById(R.id.btPlacePicker) as Button


        return view


    }
    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 12)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loading = AlertDialog.Builder(activity)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.showprogress)
        val loadingdialog = loading.create()
        loadingdialog.show()
        mAuth = FirebaseAuth.getInstance()

        val currentuser = mAuth!!.currentUser
        var location=""

        val menuref = myRef.child("Users").child(currentuser!!.uid).child("location")
        menuref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                 location=dataSnapshot.
                loadingdialog.dismiss()
                if(!dataSnapshot.hasChildren()){
//                        val builder = PlacePicker.IntentBuilder()
//                        startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
                        val locationPickerIntent = LocationPickerActivity.Builder()
                                .withLocation(34.083656, 74.797371)
                                .withGeolocApiKey("AIzaSyCkfzdwM9uVw_AqA139EyRSkwNzFKpWjt0")
                                .withSearchZone("en_In")

//                                .shouldReturnOkOnBackPressed()
//                                .withStreetHidden()
//                                .withCityHidden()
//                                .withZipCodeHidden()
                                .withSatelliteViewHidden()
                                .withGooglePlacesEnabled()
                                .withGoogleTimeZoneEnabled()
                                .withVoiceSearchHidden()
                                .build(context!!)

                        startActivityForResult(locationPickerIntent,123)

                }

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                for (snap in dataSnapshot.children) {
//                    listofcategories.add( snap.key.toString())
//                }


                // Sign in success, update UI with the signed-in user's information

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })



    }
    private fun uploadImage(offer:String,price:String) {

        if (filePath != null) {
            var progressDialog: ProgressDialog = ProgressDialog(context);
            progressDialog.setTitle("Initiating...");
            progressDialog.show();

            var ref: StorageReference = mStorageRef!!.child("images/" + currentuser!!.uid + "/offers/" + UUID.randomUUID().toString());
            ref.putFile(filePath!!)
                    .addOnSuccessListener {


                        progressDialog.dismiss();
                        var url = it.metadata!!.reference!!.downloadUrl
                        url.addOnSuccessListener {

                            var dpurl = it.toString();
                            val x=UUID.randomUUID().toString()
                            myRef.child("Offers").child(currentuser!!.uid).child(x).child("offer_image").setValue(dpurl)
                            myRef.child("Offers").child(currentuser!!.uid).child(x).child("offer_description").setValue(offer)
                            myRef.child("Offers").child(currentuser!!.uid).child(x).child("offer_price").setValue(price)
                            myRef.child("Offers").child(currentuser!!.uid).child(x).child("published").setValue("false")
                        }


                        Toast.makeText(context, "Initiated", Toast.LENGTH_SHORT).show();

                    }
                    .addOnFailureListener {

                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed ", Toast.LENGTH_SHORT).show();

                    }
                    .addOnProgressListener {

                        var progress = (100.0 * it.bytesTransferred / it
                                .getTotalByteCount())
                        progressDialog.setMessage("Initiated" + progress.toInt().toString() + "% complete");
                    }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 12 && resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data
            try {

                val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, filePath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ByteArrayOutputStream())
                Log.d("pic", filePath.toString())
                val image = chooseofferpicdialog!!.findViewById(R.id.imageView) as ImageView

                image!!.setImageBitmap(bitmap)
                image.visibility=View.VISIBLE

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK")
            if (requestCode == 1) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val postalcode = data.getStringExtra(ZIPCODE)
                Log.d("POSTALCODE****", postalcode.toString())
                val bundle = data.getBundleExtra(TRANSITION_BUNDLE)
                Log.d("BUNDLE TEXT****", bundle.getString("test"))
                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString())
                }
                val timeZoneId = data.getStringExtra(TIME_ZONE_ID)
                Log.d("TIME ZONE ID****", timeZoneId)
                val timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME)
                Log.d("TIME ZONE NAME****", timeZoneDisplayName)
            } else if (requestCode == 123) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
//                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)
//                Log.d("LekuPoi****", lekuPoi.toString())
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED")
        }
    }
}