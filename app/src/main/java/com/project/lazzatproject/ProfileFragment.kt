package com.project.lazzatproject

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class ProfileFragment : Fragment() {
    private var filePath: Uri? = null
    var e_shop_name:TextView?=null
    var e_shop_description:TextView?=null
    private val PICK_IMAGE_REQUEST = 71
    private var choosedp: Dialog? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    private var vie: View? = null
    private var mStorageRef: StorageReference? = null
    private var currentuser: FirebaseUser? = null
    private var loadingprogress: AlertDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        mStorageRef = FirebaseStorage.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        currentuser = mAuth!!.currentUser
        update()

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        vie = view
        val ownerdp = view.findViewById(R.id.owner_profile_pic) as ImageView
        val editshopname=view.findViewById(R.id.editshopname) as ImageButton
        val editshopdescription=view.findViewById(R.id.editshopdescription) as ImageButton
        val editshopaddress=view.findViewById(R.id.editshopaddress) as ImageButton
        editshopname.setOnClickListener {
            val alert=AlertDialog.Builder(context)
            var nam: EditText? = null
            with(alert) {
                setTitle("Change Shop Name ")
// Add any  input field here
                nam = EditText(context)
                nam!!.setText(e_shop_name!!.text)
                nam!!.inputType = InputType.TYPE_CLASS_TEXT
                setNeutralButton("Update") { dialog, whichButton ->
                    val mref = myRef.child("Users").child(currentuser!!.uid)
                    mref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            mref.child("shop_name").setValue(nam!!.text.toString())
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                    dialog.dismiss()

                }
                setNegativeButton("Cancel") { dialog, whichButton ->

                    //showMessage("Close the game or anything!")
                    dialog.dismiss()
                }
            }
            val dialog = alert.create()
            var container: FrameLayout  =FrameLayout(context);
            var params:FrameLayout.LayoutParams  = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin)
            nam!!.layoutParams = params;

            container.addView(nam);
            dialog.setView(container)
            dialog.show()





        }
        editshopdescription.setOnClickListener {
            val alert=AlertDialog.Builder(context)
            var des:EditText? = EditText(context)
            with(alert) {
                setTitle("Change Shop Description ")
// Add any  input field here
                des = EditText(context)
                des!!.setText(e_shop_description!!.text)
                des!!.inputType = InputType.TYPE_CLASS_TEXT
                setNeutralButton("Update") { dialog, whichButton ->
                    val mref = myRef.child("Users").child(currentuser!!.uid)
                    mref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            mref.child("shop_description").setValue(des!!.text.toString())
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                    dialog.dismiss()

                    dialog.dismiss()

                }
                setNegativeButton("Cancel") { dialog, whichButton ->

                    //showMessage("Close the game or anything!")
                    dialog.dismiss()
                }
            }
            val dialog = alert.create()
            var container: FrameLayout  =FrameLayout(context);
            var params:FrameLayout.LayoutParams  = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin)
            des!!.layoutParams = params;
            des!!.setSingleLine(false)
            des!!.setLines(4)
            container.addView(des);

            des!!.maxLines = 5
            des!!.gravity = Gravity.LEFT
            des!!.isHorizontalScrollBarEnabled = false
            dialog.setView(container)
            dialog.show()


        }
        editshopaddress.setOnClickListener {
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



        ownerdp.setOnClickListener {

            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.profilepic)
            val image = dialog.findViewById(R.id.dp1) as ImageView
            var currentuser = mAuth!!.currentUser


            if (currentuser != null) {
                val mref = myRef.child("Users").child(currentuser!!.uid)
                mref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        val username = dataSnapshot.child("name").value as String
                        val dp = dataSnapshot.child("profile_pic").value as String

                        if (dp == "none") {
                            choosedp!!.findViewById<ImageView>(R.id.dp1).setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_camera));
                        } else {
                            Picasso.get().load(dp).config(Bitmap.Config.RGB_565)
                                    .fit().centerCrop().into(image);
                        }
                        Log.d("pic", dp)
                        var usernametext = vie!!.findViewById<TextView>(R.id.owner_profile_name1)
                        usernametext.text = username
                        loadingprogress!!.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })
            }

            dialog.setCancelable(true)
            choosedp = dialog
            dialog.show()
            val changedp = dialog.findViewById(R.id.changedp) as Button
            val uploaddp = dialog.findViewById(R.id.uploaddp) as Button
            val canceldp = dialog.findViewById(R.id.canceldp) as Button
            changedp.setOnClickListener {

                chooseImage()


            }
            uploaddp.setOnClickListener {
                uploadImage()
                dialog.dismiss()
            }
            canceldp.setOnClickListener {
                //                Toast.makeText(context, "Cilcked..", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }


        }



        return view

    }


    private fun uploadImage() {

        if (filePath != null) {
            var progressDialog: ProgressDialog = ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            var ref: StorageReference = mStorageRef!!.child("images/" + currentuser!!.uid + "/" + UUID.randomUUID().toString());
            ref.putFile(filePath!!)
                    .addOnSuccessListener {


                        progressDialog.dismiss();
                        var url = it.metadata!!.reference!!.downloadUrl
                        url.addOnSuccessListener {

                            var dpurl = it.toString();
                            myRef.child("Users").child(currentuser!!.uid).child("profile_pic").setValue(dpurl)

                        }


                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();

                    }
                    .addOnFailureListener {

                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed ", Toast.LENGTH_SHORT).show();

                    }
                    .addOnProgressListener {

                        var progress = (100.0 * it.bytesTransferred / it
                                .getTotalByteCount())
                        progressDialog.setMessage("Uploaded " + progress.toInt().toString() + "%");
                    }
        }
    }


    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data
            try {

                val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, filePath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ByteArrayOutputStream())
                Log.d("pic", filePath.toString())
                val image = choosedp!!.findViewById(R.id.dp1) as ImageView
                image!!.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        else if (requestCode == 123 && data != null && data.data != null) {

            val latitude = data!!.getDoubleExtra(LATITUDE, 0.0)
            Log.d("LATITUDE****", latitude.toString())
            val longitude = data!!.getDoubleExtra(LONGITUDE, 0.0)
            Log.d("LONGITUDE****", longitude.toString())
            val address = data!!.getStringExtra(LOCATION_ADDRESS)
            Log.d("ADDRESS****", address.toString())
//                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)
//                Log.d("LekuPoi****", lekuPoi.toString())
        }
    }

    private fun update() {

        var currentuser = mAuth!!.currentUser


        if (currentuser != null) {
            val mref = myRef.child("Users").child(currentuser!!.uid)
            mref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val username = dataSnapshot.child("name").value as String
                    val dp = dataSnapshot.child("profile_pic").value as String

                    if (dp == "none") {

                        vie!!.findViewById<ImageView>(R.id.owner_profile_pic).setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_camera));
//                        Picasso.get().load(R.drawable.backprofile).into(vie!!.findViewById<ImageView>(R.id.owner_profile_pic));
                    } else {
                        Picasso.get().load(dp).config(Bitmap.Config.RGB_565)
                                .fit().centerCrop().into(vie!!.findViewById<ImageView>(R.id.owner_profile_pic));
                    }
                    Log.d("pic", dp)
                    var usernametext = vie!!.findViewById<TextView>(R.id.owner_profile_name1)
                    usernametext.text = username
                    var shop_name=vie!!.findViewById<TextView>(R.id.shopname)
                    var shop_description=vie!!.findViewById<TextView>(R.id.shopdescription)
                    var shop_address=vie!!.findViewById<TextView>(R.id.shopaddress)
                    e_shop_description=shop_description
                    e_shop_name=shop_name
                    shop_name.setText(dataSnapshot.child("shop_name").value as String)
                    shop_description.setText(dataSnapshot.child("shop_description").value as String)
                    if(dataSnapshot.child("location").hasChildren()){

                        shop_address.text=dataSnapshot.child("location").child("address").value as String
                    }
                    loadingprogress!!.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loading = AlertDialog.Builder(context)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.showprogress)
        val loadingdialog = loading.create()
        loadingprogress = loadingdialog
        loadingprogress!!.show()
//
//        var  t:Timer = Timer();
//        t.schedule(timerTask{
//            loadingprogress!!.dismiss(); // when the task active then close the dialog
//            t.cancel();
//
//        },5000)


    }
}
