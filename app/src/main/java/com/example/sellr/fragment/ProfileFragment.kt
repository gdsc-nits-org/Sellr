package com.example.sellr.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sellr.*
import com.example.sellr.R
import com.example.sellr.data.UserData
import com.example.sellr.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    val database: FirebaseDatabase =FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference:DatabaseReference=database.reference.child("Users")
    private var viewBinding: FragmentProfileBinding?=null
    private val binding get()= viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding=FragmentProfileBinding.inflate(inflater,container,false)
        val view=binding.root

        retriveDataFromDatabase()
        binding.editProfile.setOnClickListener {
            val i = Intent(context, MainFragmentHolder::class.java)
            i.putExtra("editProfile", "editProfile")
            startActivity(i)
        }
//        binding.editButton.setOnClickListener{
//            val fragmentManager:FragmentManager=requireActivity().supportFragmentManager
//            val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
//            val profileFragment=fragmentEditProfile()
//            fragmentTransaction.replace(R.id.frame,profileFragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//
//        }



//        binding.soldButton.setOnClickListener {
//            val i = Intent(activity, MainFragmentHolder::class.java)
//            i.putExtra("sold", "sold")
//            startActivity(i)
//
//        }
//
//        binding.onSaleButton.setOnClickListener {
//            val i = Intent(activity, MainFragmentHolder::class.java)
//            i.putExtra("onSale", "onSale")
//            startActivity(i)
//        }
//
//        binding.lostndFoundButton.setOnClickListener {
//
//            val i = Intent(activity, MainFragmentHolder::class.java)
//            i.putExtra("lostAndFoundList", "lostAndFoundList")
//            startActivity(i)
//        }

        return view
    }


    fun retriveDataFromDatabase(){
        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //for(eachUser in snapshot.children) {

                    val user = snapshot.child(Firebase.auth.currentUser?.uid.toString()).getValue(UserData::class.java)
                    if (user != null) {
                        //println("userId: ${user.Email}")
                        binding.EMAIL.text=user.email
                       // binding.HOSTEL.text=user.Hostel
                        binding.USERNAME.text=user.name
                        binding.SCHOLAR.text=user.scholarid
                        binding.PHONE.text=user.phonenum
                    }
                //}

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}