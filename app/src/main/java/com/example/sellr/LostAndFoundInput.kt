@file:Suppress("DEPRECATION")

package com.example.sellr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.sellr.data.LostAndFoundData
import com.example.sellr.databinding.ActivityLostAndFoundInputBinding
import com.example.sellr.utils.CheckInternet
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LostAndFoundInput : AppCompatActivity() {

    private var userUID: String? = ""
    private var emailID: String? = ""
    private var chipState: String? = ""
    private var imgUrl:String?="NONE"
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityLostAndFoundInputBinding
    private var selectedImg: Uri? = null
    private var progressCircular: ProgressBar? = null
    private lateinit var pid : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostAndFoundInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressCircular=binding.lostandfoundprogressCircular
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val user = Firebase.auth.currentUser

        if (user != null) {
            emailID = user.email
            userUID = user.uid
        } else {
            Toast.makeText(this, "Not Logged IN", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }


        database =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        storage = FirebaseStorage.getInstance("gs://sellr-7a02b.appspot.com")
        auth = FirebaseAuth.getInstance()


        binding.lostandfoundImageButton.setOnClickListener {
            ImagePicker.with(this).crop().
            compress(400).
            maxResultSize(750,750)
                .start(1000)
        }


        binding.lostandfoundInputfab.setOnClickListener {
            if(checkInternet())
            {
                val isEmpty:Boolean = prepareData()
                if (!isEmpty) {
                    uploadInfo()

                }
            }
        }
    }

    private fun prepareData(): Boolean {
        var isEmpty = false
        //val parentChipGroup: ChipGroup = findViewById(R.id.lostandfoundChipGroup)
        val parentChipGroup: ChipGroup = binding.lostandfoundChipGroup

        //if(selectedImg == null){
        //findViewById<ImageView>(R.id.lostandfoundObjectimage).visibility = View.GONE
        //}
        if (binding.lostandfoundObjectName.text!!.isEmpty()) {
            binding.ObjectInput.error = "Enter Object Name"
            isEmpty = true
        } else {
            binding.ObjectInput.error = null
        }
        if (binding.lostandfoundInputObjectLocation.text!!.isEmpty()) {
            binding.locationInput.error = "Enter Object Location"

        } else {
            binding.locationInput.error = null
        }
        if (binding.lostandfoundObjectDesc.text!!.isEmpty()) {
            binding.ObjectDesc.error = "Enter the item description"
            isEmpty = true
        } else {
            binding.ObjectDesc.error = null
        }
        if (parentChipGroup.checkedChipId == View.NO_ID) {
            findViewById<TextView>(R.id.lostandfoundChipError).visibility = View.VISIBLE
            isEmpty = true
        } else if (parentChipGroup.checkedChipId != View.NO_ID) {

            findViewById<TextView>(R.id.lostandfoundChipError).visibility = View.GONE
            chipState =
                parentChipGroup.findViewById<Chip>(parentChipGroup.checkedChipId).text.toString()


        }
        return isEmpty

    }

    private fun uploadData() {
        setProgressBar()
        val reference = storage.reference.child("LostAndFoundImages").child(Date().time.toString())
        reference.putFile(selectedImg!!).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    deleteProgressBar()
                    imgUrl=task.toString()
                    findViewById<ImageButton>(R.id.lostandfoundImageButton).setImageURI(selectedImg)
                   // Glide.with(findViewById<ImageButton>(R.id.lostandfoundObjectimage)).load(task).centerCrop().into(findViewById<ImageButton>(R.id.lostandfoundObjectimage))
                }
            }
        }

    }

    private fun uploadInfo() {

        pid = emailID!!.substringBeforeLast("@") + Date().time.toString()
        val lostAndFoundObject = LostAndFoundData(
            binding.lostandfoundObjectName.text.toString(),
            binding.lostandfoundInputObjectLocation.text.toString(),
            binding.lostandfoundObjectDesc.text.toString(),
            auth.uid.toString(),
            imgUrl,
            chipState,
            pid
        )
        setProgressBar()
        database.reference.child("LostAndFound")
            .child(pid)
            .setValue(lostAndFoundObject)
            .addOnSuccessListener {
                deleteProgressBar()
                Toast.makeText(this, "Object Successfully listed", Toast.LENGTH_SHORT).show()
                onBackPressed()
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectedImg = data.data!!
                println(selectedImg)
                uploadData()
            }
        }
    }
    private fun setProgressBar() {
        progressCircular?.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

    }

    private fun deleteProgressBar() {
        progressCircular?.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }
    override fun onBackPressed() {
        if (progressCircular?.visibility != 0) {
            super.onBackPressed()

        }

    }
    private fun checkInternet(): Boolean {
        if (CheckInternet.isConnectedToInternet(applicationContext)) {
            Toast.makeText(
                applicationContext, "Something went wrong! Check your network...",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}