package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.Adapters.OnSaleAdapter
import com.example.sellr.Adapters.SoldAdapter
import com.example.sellr.R
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentOnSaleBinding
import com.example.sellr.databinding.FragmentSoldBinding
import com.google.firebase.database.*
import java.text.FieldPosition


class SoldFragment : Fragment() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference: DatabaseReference =database.reference.child("Items")


    val itemList=ArrayList<SellData>()
    lateinit var itemsAdapter: SoldAdapter

    private var viewBinding: FragmentSoldBinding?=null
    private val binding get()= viewBinding!!


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
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                itemList.clear()   //For clearing when data gets added to database.
                for(eachItem in snapshot.children){
                    val item=eachItem.getValue(SellData::class.java)
                    if(item!=null && item.userUID=="12122"&& item.sold == true){
                        itemList.add(item)

                    }
                    itemsAdapter=SoldAdapter(activity,itemList)
                    binding.recyclerView.layoutManager= LinearLayoutManager(activity)
                    binding.recyclerView.adapter=itemsAdapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun toDelete(pId: String) {
        myReference.child(pId).removeValue()
    }


}