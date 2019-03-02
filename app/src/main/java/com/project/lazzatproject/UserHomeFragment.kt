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
    private lateinit var recyclerView: RecyclerView
    private lateinit var listCont: MutableList<Restaurant>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_home, container, false)
        recyclerView = v.findViewById<View>(R.id.recycler_view) as RecyclerView
        val viewAdapter = RecycleViewAdapter(this.context!!, listCont)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        recyclerView.adapter = viewAdapter

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(ContentValues.TAG, "inside onCreate(): ")

        listCont = ArrayList()
        Log.d(ContentValues.TAG, "above myPlace: ")

        loadPost()




    }
    private fun loadPost(){
        val loading = AlertDialog.Builder(activity)
        //View view = getLayoutInflater().inflate(R.layout.progress);
        loading.setView(R.layout.fetchingrestaurents)
        val loadingdialog = loading.create()
        loadingdialog.show()


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
                            listCont.add(Restaurant(post["name"] as String, "Food!!!", R.drawable.fifth))
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
