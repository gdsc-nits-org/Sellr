package com.example.sellr.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sellr.adapters.CartRVAdapter
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentCartBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {

    lateinit var binding : FragmentCartBinding
    private val cartModelArrayList=ArrayList<SellData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        fetchDataFromDataBase()
        return binding.root
    }

    private fun fetchDataFromDataBase() {
        binding.idRVCourse.adapter= context?.let {it1-> CartRVAdapter(it1,cartModelArrayList) }
        binding.idRVCourse.layoutManager= GridLayoutManager(context,2)
        println("Fetching data for cart screen")
        val user = Firebase.auth.currentUser
        val database= FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference =database.reference.child("Users").child(
            user?.uid.toString()).child("favpost")
        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cartModelArrayList.clear()
                if(snapshot.exists())
                {
                    for (cartItemIDs in snapshot.children) {
                        //get item ids to fetch
                        fetchIndividualItems(cartItemIDs.value.toString())
                        println(cartItemIDs.value.toString())
                    }
                }
                else
                {
                    binding.emptyIV.visibility = if (cartModelArrayList.isEmpty()) View.VISIBLE else View.GONE
                    binding.idRVCourse.visibility = if (cartModelArrayList.isEmpty()) View.GONE else View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        )
    }
    private fun fetchIndividualItems(itemID:String) {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference =database.reference.child("Items").child(itemID)
        myReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val item=snapshot.getValue(SellData::class.java)
                if (item != null) {
                    if(item.sold != true){
                        cartModelArrayList.add(item)
                    }
                }
                binding.idRVCourse.adapter?.notifyDataSetChanged()
                binding.emptyIV.visibility = if (cartModelArrayList.isEmpty()) View.VISIBLE else View.GONE
                binding.idRVCourse.visibility = if (cartModelArrayList.isEmpty()) View.GONE else View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}