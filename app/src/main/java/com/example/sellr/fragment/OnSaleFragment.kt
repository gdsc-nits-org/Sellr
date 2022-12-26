package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.Adapters.OnSaleAdapter
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentOnSaleBinding
import com.google.firebase.database.*


class OnSaleFragment : Fragment() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference: DatabaseReference =database.reference.child("Items")


    val itemList=ArrayList<SellData>()
    lateinit var itemsAdapter:OnSaleAdapter
    private lateinit var recyclerView: RecyclerView

    private var viewBinding: FragmentOnSaleBinding?=null
    private val binding get()= viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding = FragmentOnSaleBinding.inflate(inflater, container, false)
        retriveDataFromDatabase()
        return binding.root

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        retriveDataFromDatabase()
//        val layoutmanager=LinearLayoutManager(context)
//        recyclerView=binding.recyclerView
//        recyclerView.layoutManager=layoutmanager
//        recyclerView.setHasFixedSize(true)
//
//
//    }

    fun retriveDataFromDatabase(){

        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                itemList.clear()   //For clearing when data gets added to database.
                for(eachItem in snapshot.children){
                    val item=eachItem.getValue(SellData::class.java)
                    if(item!=null){
                        itemList.add(item)

                    }
                    itemsAdapter=OnSaleAdapter(activity,itemList)
                    binding.recyclerView.layoutManager=LinearLayoutManager(activity)
                    binding.recyclerView.adapter=itemsAdapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}