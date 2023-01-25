package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellr.adapters.OnSaleAdapter
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentOnSaleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class OnSaleFragment : Fragment() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val myReference: DatabaseReference =database.reference.child("Items")


    val itemList=ArrayList<SellData>()
    lateinit var itemsAdapter:OnSaleAdapter


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




    private fun retriveDataFromDatabase(){

        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=Firebase.auth.currentUser?.uid.toString()
                itemList.clear()   //For clearing when data gets added to database.
                for(eachItem in snapshot.children){
                    val item=eachItem.getValue(SellData::class.java)
                    if(item!=null && item.userUID==user&&!item.sold!!){
                        itemList.add(item)

                    }

                    itemsAdapter=OnSaleAdapter(requireContext(),itemList)
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