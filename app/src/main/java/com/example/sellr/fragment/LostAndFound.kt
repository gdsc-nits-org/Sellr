package com.example.sellr.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sellr.LostAndFoundInput
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
        

        database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        objectList = ArrayList()
        lostList = ArrayList()
        foundList = ArrayList()

        database!!.reference.child("LostAndFound")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    objectList.clear()

                    for(snapshot1 in snapshot.children){
                        val obj = snapshot1.getValue(LostAndFoundData::class.java)
                        if (obj != null) {
                            objectList.add(obj)
                        }

                        if (obj?.LostOrFound == "FOUND") {
                            foundList.add(obj)
                        }
                        else if (obj?.LostOrFound == "LOST") {
                            lostList.add(obj)
                        }
                    }
                    binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),objectList)

                    binding.lostandfoundFilterFound.setOnClickListener{
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),foundList)

                    }
                    binding.filterlost.setOnClickListener {
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),lostList)
                    }
                    binding.lostandfoundFilterAll.setOnClickListener {
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter = LostAndFoundAdapter(requireContext(),objectList)
                    }

                }



                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return binding.root
    }


}