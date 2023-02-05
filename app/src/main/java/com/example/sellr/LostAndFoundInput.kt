@file:Suppress("DEPRECATION")

package com.example.sellr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sellr.data.LostAndFoundData
import com.example.sellr.databinding.ActivityLostAndFoundInputBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LostAndFoundInput : AppCompatActivity() {

    private var userUID: String? = ""
    private var emailID: String? = ""

    private lateinit var database : FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var binding : ActivityLostAndFoundInputBinding
    private var selectedImg : Uri? = null
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostAndFoundInputBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = Firebase.auth.currentUser

        if (user != null) {
            emailID = user.email
            userUID = user.uid
        } else {
            Toast.makeText(this,"Not Logged IN",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }


        dialog = AlertDialog.Builder(this)
            .setMessage("Uploading Picture..")
            .setCancelable(false)

        database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        storage = FirebaseStorage.getInstance("gs://sellr-7a02b.appspot.com")
        auth = FirebaseAuth.getInstance()


        binding.lostandfoundImageButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 2)

            //val intent = Intent(Intent.ACTION_PICK)
            //intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            //startActivityForResult(intent, 69)
        }

        binding.lostandfoundInputfab.setOnClickListener {

            //if(selectedImg == null){
                //findViewById<ImageView>(R.id.lostandfoundObjectimage).visibility = View.GONE
            //}
            if(binding.lostandfoundObjectName.text!!.isEmpty()){
                Toast.makeText(this,"Enter Object Name",Toast.LENGTH_SHORT).show()
            }
            else if(binding.lostandfoundInputObjectLocation.text!!.isEmpty()){
                Toast.makeText(this,"Enter Object Location",Toast.LENGTH_SHORT).show()
            }
            else if(binding.lostandfoundInputUserContact.text!!.isEmpty()){
                Toast.makeText(this,"Enter your contact ",Toast.LENGTH_SHORT).show()
            }
            else if (selectedImg == null){
                uploadInfo("NONE")
            }
            else{
                uploadData()
            }

        }
    }

    private fun uploadData() {
        val reference = storage.reference.child("LostAndFoundImages").child(Date().time.toString())
        reference.putFile(selectedImg!!).addOnCompleteListener{
            if(it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }

    }

    private fun uploadInfo(imgUrl: String) {
        val lostAndFoundObject = LostAndFoundData(binding.lostandfoundObjectName.text.toString(),binding.lostandfoundInputObjectLocation.text.toString(),binding.lostandfoundInputUserContact.text.toString(),auth.uid.toString(),imgUrl )
        database.reference.child("LostAndFound")
            .child(emailID!!.substringBeforeLast("@")+Date().time.toString())
            .setValue(lostAndFoundObject)
            .addOnSuccessListener {
                Toast.makeText(this,"Object listed",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null ){
            if(data.data != null){
                selectedImg = data.data!!

                //findViewById<ImageView>(R.id.lostandfoundObjectimage).setImageURI(selectedImg)
            }
        }
    }
}