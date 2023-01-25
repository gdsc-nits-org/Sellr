package com.example.sellr.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sellr.R
import com.example.sellr.data.UserData
import com.example.sellr.databinding.FragmentEditProfileBinding
import com.example.sellr.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class fragmentEditProfile : Fragment() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference: DatabaseReference =database.reference.child("Users")
    private var viewBinding: FragmentEditProfileBinding?=null
    private val binding get()= viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentEditProfileBinding.inflate(inflater,container,false)
        setData()
        binding.updateButton.setOnClickListener{
            updateData()
        }

        return binding.root
    }

    fun setData(){
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //for(eachUser in snapshot.children) {
                val user = snapshot.child(Firebase.auth.currentUser?.uid.toString()).getValue(UserData::class.java)
                if (user != null) {
                    //println("userId: ${user.Email}")
                   // binding.editHostelNumber.setText(user.Hostel)
                    binding.editUserName.setText(user.name)
                    binding.editScholarID.setText(user.scholarid)
                    binding.editPhoneNumber.setText(user.phonenum)
                }
                //}

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateData(){

        val updatedName=binding.editUserName.text.toString()
        val updatedScholarId=binding.editScholarID.text.toString()
        val updatedPhoneNumber=binding.editPhoneNumber.text.toString()
        //val updatedHostel=binding.editHostelNumber.text.toString()
        val userId=Firebase.auth.currentUser?.uid.toString()
        println("userId: ${userId}")
        val userMap= mutableMapOf<String,Any>()
        userMap["name"]=updatedName
       // userMap["Hostel"]=updatedHostel
        userMap["phonenum"]=updatedPhoneNumber
        userMap["scholarid"]=updatedScholarId
        myReference.child(userId).updateChildren(userMap).addOnCompleteListener{ task->
            if(task.isSuccessful){
                Toast.makeText(activity,"The user has been updated",Toast.LENGTH_LONG).show()
                val fragmentManager: FragmentManager =requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction =fragmentManager.beginTransaction()
                val profileFragment=ProfileFragment()
                fragmentTransaction.replace(R.id.fragmentContainer,profileFragment)
                fragmentTransaction.commit()

            }
        }

    }
}