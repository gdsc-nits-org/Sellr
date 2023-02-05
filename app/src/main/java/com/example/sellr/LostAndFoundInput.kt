package com.example.sellr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sellr.data.LostAndFoundData
import com.example.sellr.databinding.ActivityLostAndFoundInputBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LostAndFoundInput : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private lateinit var binding : ActivityLostAndFoundInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostAndFoundInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lostandfoundInputfab.setOnClickListener {

            val objectName = binding.lostandfoundObjectName.text.toString()
            val objectLocation = binding.lostandfoundInputObjectLocation.text.toString()
            val phoneNum = binding.lostandfoundInputUserContact.text.toString()

            database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LostAndFoundObjects")

            val lostAndFoundObject = LostAndFoundData(objectName, objectLocation , phoneNum)
            database.child(phoneNum).setValue(lostAndFoundObject).addOnSuccessListener {
                binding.lostandfoundObjectName.text?.clear()
                binding.lostandfoundInputObjectLocation.text?.clear()
                binding.lostandfoundInputUserContact.text?.clear()

                Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }
        }

    }
}