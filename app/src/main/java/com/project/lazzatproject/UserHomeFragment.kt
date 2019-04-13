package com.project.lazzatproject


import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class UserHomeFragment : Fragment() {


    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    private lateinit var v: View
    private lateinit var offerview: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var listCont: MutableList<Restaurant>
    private lateinit var listOffer: MutableList<Offer>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_home, container, false)

        offerview=v.findViewById(R.id.offers_recycler_view) as RecyclerView
        val offerviewAdapter = OfferRecycleViewAdapter(this.context!!, listOffer)
        offerview.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.HORIZONTAL,false)
        offerview.adapter = offerviewAdapter

        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView
        val viewAdapter = RecycleViewAdapter(this.context!!, listCont)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = viewAdapter

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(ContentValues.TAG, "inside onCreate(): ")

        listCont = ArrayList()
        listOffer=ArrayList()
        Log.d(ContentValues.TAG, "above myPlace: ")

        loadPost()




    }
    private fun loadPost(){
        val loading = AlertDialog.Builder(activity)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.fetchingrestaurents)
        val loadingdialog = loading.create()
        loadingdialog.show()
        listOffer.add(Offer("hi"))
        listOffer.add(Offer("hey"))
        listOffer.add(Offer("what up?"))


        myRef.child("Users").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    Log.d(ContentValues.TAG, "inside onDataChange(): ")

                    val td = p0.value as HashMap<*, *>

                    for(key in td.keys){
                        Log.d(ContentValues.TAG, "inside for loop: ")

                        val post = td[key] as HashMap<*, *>
                        if(post["type"] as String == "owner") {
                            Log.d(ContentValues.TAG, "inside if(): ")
                            listCont.add(Restaurant(post["name"] as String, post["shop_description"] as String,post["profile_pic"] as String,post["email"] as String,key as String))
                        }
                    }

                }catch (ex:Exception){}
                if (isVisible){

                    val viewAdapter = RecycleViewAdapter(context!!,listCont)
                    recyclerView.adapter = viewAdapter
                    loadingdialog.dismiss()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

}
