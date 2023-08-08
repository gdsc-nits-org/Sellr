@file:Suppress("DEPRECATION")

package com.gdsc.sellr

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.gdsc.sellr.dataModels.LostAndFoundDataModel
import com.gdsc.sellr.databinding.ActivityLostAndFoundInputBinding
import com.gdsc.sellr.utils.CheckInternet
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class LostAndFoundInput : AppCompatActivity() {

    private var userUID: String? = ""
    private var emailID: String? = ""
    private var chipState: String? = ""
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLostAndFoundInputBinding
    private var progressCircular: LottieAnimationView? = null
    private lateinit var pid : String
    private var imagePrimary: String? = ""
    private var imageArray = ArrayList<String>()
    private var imageButtonPrimary: ImageButton? = null
    private var imageButtonSecond: ImageButton? = null
    private var imageButtonThird: ImageButton? = null
    private var imageButtonFourth: ImageButton? = null
    private lateinit var uploadTask: UploadTask
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostAndFoundInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressCircular=binding.progressAnimationViewLnf
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        val user = Firebase.auth.currentUser



        imageArray.add("")
        imageArray.add("")
        imageArray.add("")



        if (user != null) {
            emailID = user.email
            userUID = user.uid
        } else {
            Toast.makeText(this, "Not Logged IN", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }


        database =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app/")
        auth = FirebaseAuth.getInstance()



        imageButtonPrimary = binding.lostandfoundImageButtonFirst
        imageButtonSecond =  binding.lostandfoundImageButtonSecond
        imageButtonThird =  binding.lostandfoundImageButtonThird
        imageButtonFourth =  binding.lostandfoundImageButtonFourth
        imageButtonSecond?.isEnabled = false
        imageButtonThird?.isEnabled = false
        imageButtonFourth?.isEnabled = false


        imageButtonPrimary?.setOnClickListener {
            if (checkInternet()) {
                ImagePicker.with(this).crop().
                compress(250).
                maxResultSize(600,600).
                start(1000)
            }
        }

        imageButtonSecond?.setOnClickListener {
            if (checkInternet()) {
                ImagePicker.with(this).crop().
                compress(250).
                maxResultSize(600,600).
                start(2000)
            }
        }
        imageButtonThird?.setOnClickListener {
            if (checkInternet()) {
                ImagePicker.with(this).crop().
                compress(250).
                maxResultSize(600,600).
                start(3000)
            }
        }
        imageButtonFourth?.setOnClickListener {
            if (checkInternet()) {
                ImagePicker.with(this).crop().
                compress(250).
                maxResultSize(600,600).
                start(4000)
            }
        }

        binding.lostandfoundInputfab.setOnClickListener {
            if(checkInternet())
            {
                val isEmpty:Boolean = prepareData()
                if (!isEmpty) {

                    deleteProgressBar()
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Are you sure?")
                    builder.setMessage("This post will be made public")
                    builder.setPositiveButton("Yes") { _, _ ->
                        uploadInfo()
                    }
                    builder.setNegativeButton("No") { _, _ ->
                    }
                    builder.show()
                }
            }
        }

        binding.datePickButton.setOnClickListener {
            clickDatePicker()
        }
    }

    private fun clickDatePicker(){

        val myCalendar = Calendar.getInstance();
        val year=myCalendar.get(Calendar.YEAR);
        val month=myCalendar.get(Calendar.MONTH);
        val day=myCalendar.get(Calendar.DAY_OF_MONTH);



        val dpd= DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                 selectedDate="$selectedDayOfMonth/${selectedMonth+1}/$selectedYear ";
                binding.chosenDate.text =selectedDate
            },
            year,
            month,
            day

        )
        //we are setting the date picker so that dates before yesterday cant be picked
        //system.currentTimesMillis gives the current time and 86400000 is the number of milliseconds in a day
        dpd.datePicker.maxDate = System.currentTimeMillis()-8640000
        dpd.show();
    }

    private fun prepareData(): Boolean {
        var isEmpty = false
        val parentChipGroup: ChipGroup = binding.lostandfoundChipGroup
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

//    private fun uploadData() {
//        setProgressBar()
//        val reference = storage.reference.child("LostAndFoundImages").child(Date().time.toString())
//        reference.putFile(selectedImg!!).addOnCompleteListener {
//            if (it.isSuccessful) {
//                reference.downloadUrl.addOnSuccessListener { task ->
//                    deleteProgressBar()
//                    imgUrl=task.toString()
//                    //findViewById<ImageButton>(R.id.lostandfoundImageButtonFirst).setImageURI(selectedImg)
//
//                   binding.lostandfoundImageButtonFirst.setImageURI(selectedImg)
//                   // Glide.with(findViewById<ImageButton>(R.id.lostandfoundObjectimage)).load(task).centerCrop().into(findViewById<ImageButton>(R.id.lostandfoundObjectimage))
//                }
//            }
//        }
//    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadInfo() {

        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        val datetime: String = ft.format(dNow)
        pid = emailID!!.substringBeforeLast("@") + datetime

        val lostAndFoundObject = LostAndFoundDataModel(
            binding.lostandfoundObjectName.text.toString(),
            binding.lostandfoundInputObjectLocation.text.toString(),
            binding.lostandfoundObjectDesc.text.toString(),
            auth.uid.toString(),
            imagePrimary,
            imageArray,
            chipState,
            pid,
            selectedDate
        )
        setProgressBar()
        database.reference.child("LostAndFound")
            .child(pid)
            .setValue(lostAndFoundObject)
            .addOnSuccessListener {
                deleteProgressBar()
                closeKeyboard()
                binding.LandFScrollView.visibility=View.GONE
                binding.successAnimationViewLnf.visibility=View.VISIBLE
                binding.lostandfoundInputfab.visibility=View.INVISIBLE
                supportActionBar?.hide()
                Toast.makeText(this, "Object Successfully listed", Toast.LENGTH_SHORT).show()
                binding.successAnimationViewLnf.playAnimation()

                Handler(Looper.getMainLooper()).postDelayed({

                    onBackPressed()
                    finish()
                }, 2000)

            }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1000 -> {

                    upDateImage(1, data)
                }
                2000 -> {
                    upDateImage(2, data)
                }
                3000 -> {
                    upDateImage(3, data)
                }
                4000 -> {
                    upDateImage(4, data)
                }
            }
        }
    }

    private fun upDateImage(i: Int, data: Intent?) {
        setProgressBar()
        val storageRef = Firebase.storage.reference

        val imageUri: Uri? = data?.data

        val filename = generateUID(emailID + i.toString())

        uploadTask = storageRef.child("LostAndFoundImages/$filename").putFile(imageUri!!)


        //if even the delay specified the progress bar is still visible
        //it means that the image was not uploaded
        //return no connection issue
        Handler(Looper.getMainLooper()).postDelayed({
            if (progressCircular?.visibility == View.VISIBLE) {
                Toast.makeText(this, "Time out!!Check Your connection", Toast.LENGTH_LONG).show()
                deleteProgressBar()
                uploadTask.cancel()
            }

        }, 300000)


        //this part is used to update the small image icons in the sell window
        uploadTask.addOnSuccessListener {
            storageRef.child("LostAndFoundImages/$filename").downloadUrl.addOnSuccessListener {

                when (i) {
                    1 -> {
                        if (imagePrimary != "") {
                            val ref = Firebase.storage.getReferenceFromUrl(imagePrimary!!)
                            ref.delete()
                        }

                        imageButtonPrimary?.setImageURI(imageUri)
                        imagePrimary = it.toString()
                        imageButtonSecond?.isEnabled = true
                        imageButtonSecond?.visibility = View.VISIBLE
                        imageButtonSecond?.setImageResource(R.drawable.ic_image_placeholder)

                    }
                    2 -> {
                        if (imageArray[0] != "") {
                            val ref = Firebase.storage.getReferenceFromUrl(imageArray[0])
                            ref.delete()
                        }
                        imageButtonSecond?.setImageURI(imageUri)
                        imageArray[0] = it.toString()
                        imageButtonThird?.isEnabled = true
                        imageButtonThird?.visibility = View.VISIBLE
                        imageButtonThird?.setImageResource(R.drawable.ic_image_placeholder)
                    }
                    3 -> {
                        imageButtonThird?.setImageURI(imageUri)
                        if (imageArray[1] != "") {
                            val ref = Firebase.storage.getReferenceFromUrl(imageArray[2])
                            ref.delete()
                        }
                        imageArray[1] = it.toString()
                        imageButtonFourth?.isEnabled = true
                        imageButtonFourth?.visibility = View.VISIBLE
                        imageButtonFourth?.setImageResource(R.drawable.ic_image_placeholder)
                    }
                    4 -> {
                        imageButtonFourth?.setImageURI(imageUri)
                        if (imageArray[2] != "") {
                            val ref = Firebase.storage.getReferenceFromUrl(imageArray[2])
                            ref.delete()
                        }
                        imageArray[2] = it.toString()
                    }
                }
                deleteProgressBar()
            }

        }.addOnFailureListener {
            makeToast("Upload Failed")
            deleteProgressBar()
        }

    }

    private fun makeToast(value: String) {

        Toast.makeText(applicationContext, value, Toast.LENGTH_LONG).show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun generateUID(emailID: String): String {

        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        val datetime: String = ft.format(dNow)
        return emailID.substringBeforeLast("@") + datetime
    }



    private fun setProgressBar() {
        progressCircular?.playAnimation()
        progressCircular?.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

    }

    private fun deleteProgressBar() {
        progressCircular?.cancelAnimation()
        progressCircular?.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }
    override fun onBackPressed() {
        if (progressCircular?.visibility != 0 && !binding.successAnimationViewLnf.isAnimating) {
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
    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view = this.currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}