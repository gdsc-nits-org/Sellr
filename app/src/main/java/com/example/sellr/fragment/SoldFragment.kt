package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellr.adapters.SoldAdapter
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentSoldBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class SoldFragment : Fragment() {


    val itemList = ArrayList<SellData>()
    lateinit var itemsAdapter: SoldAdapter

    private var viewBinding: FragmentSoldBinding? = null
    private val binding get() = viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentSoldBinding.inflate(inflater, container, false)
        retriveDataFromDatabase()
        return binding.root

    }

    private fun retriveDataFromDatabase() {
        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference = database.reference.child("Items")
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = Firebase.auth.currentUser?.uid.toString()
                itemList.clear()   //For clearing when data gets added to database.
                for (eachItem in snapshot.children) {
                    val item = eachItem.getValue(SellData::class.java)
                    if (item != null && item.userUID == user && item.sold == true) {
                        itemList.add(item)

                    }
                    itemsAdapter = SoldAdapter(requireContext(),itemList)
                    binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    binding.recyclerView.adapter = itemsAdapter


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}