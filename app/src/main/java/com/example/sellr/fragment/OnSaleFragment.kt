package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sellr.R
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentOnSaleBinding
import com.example.sellr.databinding.FragmentProfileBinding
import com.google.firebase.database.*


class OnSaleFragment : Fragment() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference: DatabaseReference =database.reference.child("Items")


    private var viewBinding: FragmentOnSaleBinding?=null
    private val binding get()= viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding=FragmentOnSaleBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    fun retriveDataFromDatabase(){
        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(eachItem in snapshot.children){
                    val item=eachItem.getValue(SellData::class.java)
                    if(item!=null){

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}