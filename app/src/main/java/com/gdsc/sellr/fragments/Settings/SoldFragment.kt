package com.gdsc.sellr.fragments.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.gdsc.sellr.adapters.SoldItemsAdapter
import com.gdsc.sellr.dataModels.SellDataModel
import com.gdsc.sellr.databinding.FragmentSoldBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class SoldFragment : Fragment() {


    val itemList = ArrayList<SellDataModel>()
    lateinit var itemsAdapter: SoldItemsAdapter

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

        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference =database.reference.child("Items")

        myReference.get().addOnSuccessListener {
            val user=Firebase.auth.currentUser?.uid.toString()
            itemList.clear()   //For clearing when data gets added to database.

            for(eachItem in it.children){
                val item=eachItem.getValue(SellDataModel::class.java)
                if(item!=null && item.userUID==user&&item.sold!!){
                    itemList.add(item)
                }

                itemsAdapter= SoldItemsAdapter(requireContext(),itemList)
                binding.recyclerView.layoutManager=GridLayoutManager(context,2)
                binding.recyclerView.adapter=itemsAdapter
                binding.emptyState.visibility = if (itemList.isEmpty()) View.VISIBLE else View.GONE
                binding.recyclerView.visibility = if (itemList.isEmpty()) View.GONE else View.VISIBLE
            }

        }.addOnFailureListener {

        }

    }


}