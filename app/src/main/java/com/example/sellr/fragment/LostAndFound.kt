package com.example.sellr.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sellr.LostAndFoundInput
import com.example.sellr.R
import com.example.sellr.adapters.LostAndFoundAdapter
import com.example.sellr.data.LostAndFoundData
import com.example.sellr.databinding.FragmentLostAndFoundBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LostAndFound : Fragment() {


    lateinit var binding :FragmentLostAndFoundBinding
    private var database : FirebaseDatabase ? = null
    lateinit var objectList: ArrayList<LostAndFoundData>
    lateinit var foundList : ArrayList<LostAndFoundData>
    lateinit var lostList : ArrayList<LostAndFoundData>
    lateinit var refreshLostAndFound : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLostAndFoundBinding.inflate(layoutInflater)
        val listit = binding.lostandfoundFab
        listit.setOnClickListener {
            val lostandfoundInput = Intent(context, LostAndFoundInput::class.java)
            startActivity(lostandfoundInput)
        }


        refreshLostAndFound = binding.lostandfoundSwipeRefresh
        database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        objectList = ArrayList()
        lostList = ArrayList()
        foundList = ArrayList()

        database!!.reference.child("LostAndFound")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    objectList.clear()
                    foundList.clear()
                    lostList.clear()

                    for(snapshot1 in snapshot.children){
                        val obj = snapshot1.getValue(LostAndFoundData::class.java)
                        if (obj != null) {
                            objectList.add(obj)
                        }

                        if (obj?.lostOrFound == "FOUND") {
                            foundList.add(obj)
                        }

                        else if (obj?.lostOrFound == "LOST") {
                            lostList.add(obj)
                        }
                    }


                    binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),objectList)

                    refreshLostAndFound.setOnRefreshListener {
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),objectList)
                        refreshLostAndFound.isRefreshing = false

                    }

                    binding.lostandfoundFilterFound.setOnClickListener{
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),foundList)

                        refreshLostAndFound.setOnRefreshListener {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                            binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),foundList)
                            refreshLostAndFound.isRefreshing = false
                        }

                    }
                    binding.filterlost.setOnClickListener {
                        binding.filterlost.isSelected != binding.filterlost.isSelected


                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),lostList)

                        refreshLostAndFound.setOnRefreshListener {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                            binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),lostList)
                            refreshLostAndFound.isRefreshing = false
                        }
                    }
                    binding.lostandfoundFilterAll.setOnClickListener {
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),objectList)

                        refreshLostAndFound.setOnRefreshListener {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                            binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),objectList)
                            refreshLostAndFound.isRefreshing = false
                        }
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return binding.root
    }


}