package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sellr.R
import com.example.sellr.data.UserData
import com.example.sellr.databinding.FragmentProfileBinding
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class ProfileFragment : Fragment() {

    val database: FirebaseDatabase =FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference:DatabaseReference=database.reference.child("Users")
    private var viewBinding: FragmentProfileBinding?=null
    private val binding get()= viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentProfileBinding.inflate(inflater,container,false)
        val view=binding.root

        retriveDataFromDatabase()

        binding.editButton.setOnClickListener{
            val fragmentManager:FragmentManager=requireActivity().supportFragmentManager
            val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
            val profileFragment=fragmentEditProfile()
            fragmentTransaction.replace(R.id.frame,profileFragment)
            fragmentTransaction.commit()

        }

        binding.soldButton.setOnClickListener {
            val fragmentManager:FragmentManager=requireActivity().supportFragmentManager
            val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
            val soldFragment=SoldFragment()
            fragmentTransaction.replace(R.id.frame,soldFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        binding.onSaleButton.setOnClickListener {
            val fragmentManager:FragmentManager=requireActivity().supportFragmentManager
            val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
            val onSaleFragment=OnSaleFragment()
            fragmentTransaction.replace(R.id.frame,onSaleFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }


    fun retriveDataFromDatabase(){
        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //for(eachUser in snapshot.children) {
                    val user = snapshot.child(12122.toString()).getValue(UserData::class.java)
                    if (user != null) {
                        //println("userId: ${user.Email}")
                        binding.EMAIL.text=user.Email
                        binding.HOSTEL.text=user.Hostel
                        binding.USERNAME.text=user.Name
                        binding.SCHOLAR.text=user.ScholarId
                        binding.PHONE.text=user.Phone
                    }
                //}

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}