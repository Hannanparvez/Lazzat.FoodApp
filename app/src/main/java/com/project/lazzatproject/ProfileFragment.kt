package com.project.lazzatproject

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
import android.widget.TextView
import kotlinx.android.synthetic.main.profilepic.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {
    private var dop: ImageView? = null

    private var filePath: Uri? = null

    private val PICK_IMAGE_REQUEST = 71


    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mAuth: FirebaseAuth? = null
    private  var vi:View?=null
    private var vie:View?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        mAuth = FirebaseAuth.getInstance()


        update()
        vi = inflater.inflate(R.layout.profilepic,container,false)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        vie=view
        val ownerdp = view.findViewById(R.id.owner_profile_pic) as ImageView
        dop=vi!!.findViewById(R.id.dp) as ImageView
        ownerdp.setOnClickListener {

            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.profilepic)
            dialog.setCancelable(true)
            dialog.show()
            val changedp = dialog.findViewById(R.id.changedp) as Button
            val uploaddp = dialog.findViewById(R.id.uploaddp) as Button
            val canceldp = dialog.findViewById(R.id.canceldp) as Button
            changedp.setOnClickListener{
                chooseImage()

            }
            canceldp.setOnClickListener{
//                Toast.makeText(context, "Cilcked..", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }


        }



        return view

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

                dop!!.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun update(){

        var currentuser=mAuth!!.currentUser


        if (currentuser!=null){
            val mref=myRef.child("Users").child(currentuser!!.uid)
            mref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val username = dataSnapshot.child("name").value as String
                    var usernametext= vie!!.findViewById<TextView>(R.id.owner_profile_name1)
                    usernametext.text = username
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
        }
    }


//    private fun upload() {
//
//        if (filePath != null) {
//            val progressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Uploading...")
//            progressDialog.show()
//
//            val ref = storageReference.child("images/" + UUID.randomUUID().toString())
//            ref.putFile(filePath)
//                    .addOnSuccessListener(OnSuccessListener<Any> {
//                        progressDialog.dismiss()
//                        Toast.makeText(this@MainActivity, "Uploaded", Toast.LENGTH_SHORT).show()
//                    })
//                    .addOnFailureListener(OnFailureListener { e ->
//                        progressDialog.dismiss()
//                        Toast.makeText(this@MainActivity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
//                    })
//                    .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot>() {
//                        fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
//                            val progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                                    .getTotalByteCount()
//                            progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
//                        }
//                    })
//        }
//    }
}