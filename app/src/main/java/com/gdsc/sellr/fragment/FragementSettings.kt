package com.gdsc.sellr.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.gdsc.sellr.AuthActivity
import com.gdsc.sellr.MainFragmentHolder
import com.gdsc.sellr.dataModels.UserDataModel
import com.gdsc.sellr.databinding.FragmentFragementSettingsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FragementSettings : Fragment() {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference: DatabaseReference =database.reference.child("Users")
    private var viewBinding:FragmentFragementSettingsBinding?= null
    private val binding get()= viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewBinding= FragmentFragementSettingsBinding.inflate(inflater,container,false)
        retriveDataFromDatabase()

        binding.profile.setOnClickListener {
            val i = Intent(activity, MainFragmentHolder::class.java)
            i.putExtra("profile", "profile")
            startActivity(i)
        }

        binding.soldButton.setOnClickListener {
            val i = Intent(activity, MainFragmentHolder::class.java)
            i.putExtra("sold", "sold")
            startActivity(i)

        }

        binding.onSaleButton.setOnClickListener {
            val i = Intent(activity, MainFragmentHolder::class.java)
            i.putExtra("onSale", "onSale")
            startActivity(i)
        }

        binding.lostAndFound.setOnClickListener {

            val i = Intent(activity, MainFragmentHolder::class.java)
            i.putExtra("lostAndFoundList", "lostAndFoundList")
            startActivity(i)
        }

        binding.signOut.setOnClickListener{
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Are you sure?")
            builder.setMessage("You will be logged out of your account")
            builder.setPositiveButton("Yes") { _, _ ->
                Firebase.auth.signOut()
                val intent = Intent(context, AuthActivity::class.java)
                intent.putExtra("splash off", "splash off")
                startActivity(intent)
                activity?.finish()

            }
            builder.setNegativeButton("No") { _, _ ->
            }
            builder.show()

        }
        return binding.root
    }

    private fun retriveDataFromDatabase(){
        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //for(eachUser in snapshot.children) {

                val user = snapshot.child(Firebase.auth.currentUser?.uid.toString()).getValue(
                    UserDataModel::class.java)
                if (user != null) {
                    //println("userId: ${user.Email}")
                    binding.userName.text=user.name
                }
                //}

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}