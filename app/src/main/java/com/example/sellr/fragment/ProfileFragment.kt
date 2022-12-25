package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sellr.R
import com.example.sellr.databinding.FragmentProfileBinding
import com.google.firebase.database.*

class ProfileFragment : Fragment() {


    val database: FirebaseDatabase =FirebaseDatabase.getInstance()
    val myReference:DatabaseReference=database.reference.child("Users")
    private var viewBinding: FragmentProfileBinding?=null
    private val binding get()= viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentProfileBinding.inflate(inflater,container,false)
        val view=binding.root



        return view
    }

    fun retriveDataFromDatabase(){
        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}