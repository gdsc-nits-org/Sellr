package com.gdsc.sellr.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gdsc.sellr.data.UserData
import com.gdsc.sellr.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class fragmentEditProfile : Fragment() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val myReference: DatabaseReference =database.reference.child("Users")
    private var viewBinding: FragmentEditProfileBinding?=null
    private val binding get()= viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding=FragmentEditProfileBinding.inflate(inflater,container,false)
        setData()
        binding.updateButton.setOnClickListener{
            updateData()
        }

        return binding.root
    }

    private fun setData(){
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.child(Firebase.auth.currentUser?.uid.toString()).getValue(UserData::class.java)
                if (user != null) {
                    binding.editUserName.setText(user.name)
                    binding.editPhoneNumber.setText(user.phonenum)
                    if(user.scholarid=="-------")
                    {
                        binding.editScholarID.setText("");
                    }
                    else
                    {
                        binding.editScholarID.setText(user.scholarid)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun updateData(){

        val updatedName=binding.editUserName.text.toString()
        var updatedScholarId=binding.editScholarID.text.toString()
        val updatedPhoneNumber=binding.editPhoneNumber.text.toString()
        var checkEmpty=false
        if(updatedName=="")
        {
            checkEmpty=true
            binding.getUser.error="This field is required"
        }
        else
        {
            binding.getUser.error=null
        }
        if(updatedPhoneNumber=="")
        {
            checkEmpty=true
            binding.getPhone.error="This field is required"
        }
        else
        {
            binding.getPhone.error=null
        }
        if(updatedScholarId=="")
        {
            updatedScholarId="-------"
        }
        if(!checkEmpty)
        {
            val userId=Firebase.auth.currentUser?.uid.toString()
            val userMap= mutableMapOf<String,Any>()
            userMap["name"]=updatedName
            userMap["phonenum"]=updatedPhoneNumber
            userMap["scholarid"]=updatedScholarId
            myReference.child(userId).updateChildren(userMap).addOnCompleteListener{ task->
                if(task.isSuccessful){
                    Toast.makeText(requireContext(),"The user data has been updated",Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()

                }
            }
        }



    }
}