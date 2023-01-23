package com.example.sellr

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class fragment_extradetails : Fragment() {

    private lateinit var phonenum:EditText
    private lateinit var name:EditText
    private lateinit var scholarid:EditText
    private lateinit var submit:Button
    private lateinit var dtb:DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view =  inflater.inflate(R.layout.fragment_extradetails, container, false)

        phonenum = view.findViewById(R.id.editTextphone)
        name = view.findViewById(R.id.editTextname)
        scholarid = view.findViewById(R.id.editTextid)
        dtb = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        submit = view.findViewById(R.id.button)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        submit.setOnClickListener {
            val phone = phonenum.text.toString().trim()
            val nametxt = name.text.toString().trim()
            val id = scholarid.text.toString().trim()
            if(phone.isBlank()|| nametxt.isBlank())
                phonenum.setError("Aestrik fields are required")
            else
            {
                dtb.child("Users").child(user.uid.toString()).child("infoentered").setValue("yes")
                dtb.child("Users").child(user.uid).child("phonenum").setValue(phone)
                        dtb.child("Users").child(user.uid).child("name").setValue(nametxt)
                        dtb.child("Users").child(user.uid).child("scholarid").setValue(id)
                if(activity?.intent!!.hasExtra("extraDetails"))
                {
                    activity?.onBackPressed()
                }
                else
                {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                }



            }

        }











        return view
    }


}