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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class UserProfileFragment : Fragment() {
    private var filePath: Uri? = null
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
        Log.d("pic", currentuser!!.email)
        update()

        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        vie = view
        val userdp = view.findViewById(R.id.user_profile_photo) as ImageView


        userdp.setOnClickListener {

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
                            Log.d("hanna","hanan")
                            choosedp!!.findViewById<ImageView>(R.id.dp1).setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_camera));
                        } else {
                            Picasso.get().load(dp).config(Bitmap.Config.RGB_565)
                                    .fit().centerCrop().into(image);
                        }

                        var usernametext = vie!!.findViewById<TextView>(R.id.user_profile_name)
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
                    Log.d("pic",dp)

                    if (dp == "none") {
                        vie!!.findViewById<ImageView>(R.id.user_profile_photo).setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_camera));
                    } else {
                        Picasso.get().load(dp).config(Bitmap.Config.RGB_565)
                                .fit().centerCrop().into(vie!!.findViewById<ImageView>(R.id.user_profile_photo));
                    }
                    Log.d("pic", dp)
                    var usernametext = vie!!.findViewById<TextView>(R.id.user_profile_name)
                    usernametext.text = username
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
        val loading = AlertDialog.Builder(activity)
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
