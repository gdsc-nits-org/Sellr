package com.example.sellr

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sellr.databinding.ActivityDescrptionPageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class DescriptionPage : AppCompatActivity() {
    private lateinit var binding: ActivityDescrptionPageBinding
    private lateinit var imageList: ArrayList<SlideModel>
    private lateinit var imageSlider: ImageSlider
    //fab idea
    private lateinit var backDrop: View
    private lateinit var lytMic: View
    private lateinit var lytCall: View
    private var rotate = false
    private var emailSeller=""
    private var phoneSeller=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescrptionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            this.supportActionBar!!.hide()
        } // catch block to handle NullPointerException
        catch (_: NullPointerException) {
        }
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key").toString()
            fetchDataFromDataBase(value)
        }

        imageList = ArrayList()
        imageSlider = findViewById(R.id.image_slider)



        backDrop = binding.backDrop
        lytMic = binding.lytMic
        lytCall = binding.lytCall

        val fabMic: FloatingActionButton = binding.fabMic
        val fabCall: FloatingActionButton = binding.fabCall
        val fabAdd: FloatingActionButton = binding.fabAdd

        initShowOut(lytMic)
        initShowOut(lytCall)

        backDrop.visibility = View.GONE

        fabAdd.setOnClickListener { v: View ->
            toggleFabMode(v)
        }

        backDrop.setOnClickListener {
            toggleFabMode(fabAdd)
        }

        lytMic.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailSeller")
            startActivity(intent)
        }

        lytCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneSeller")
            startActivity(intent)
        }
        fabMic.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailSeller")
            startActivity(intent)
        }

        fabCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneSeller")
            startActivity(intent)
        }
    }


    //For fab group display
    private fun toggleFabMode(v: View) {
        rotate =  !rotate
        if (rotate) {
            showIn(lytMic)
            showIn(lytCall)
            backDrop.visibility = View.VISIBLE
        } else {
            showOut(lytMic)
            showOut(lytCall)
            backDrop.visibility = View.GONE
        }
    }

    private fun initShowOut(v: View) {
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }

    private fun showIn(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1f)
            .start()
    }

    private fun showOut(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
            .setDuration(200)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
    }




    private fun fetchDataFromDataBase(items:String)
    {
        val databaseItem = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")
        databaseItem.child(items).get().addOnSuccessListener { dataSnapshot ->

            if (dataSnapshot.exists()) {
                val primaryImage = dataSnapshot.child("imagePrimary").value.toString()
                imageList.add(SlideModel(primaryImage))
                val imageArray = ArrayList<String>()
                imageArray.add(dataSnapshot.child("imageList").child("0").value.toString())
                imageArray.add(dataSnapshot.child("imageList").child("1").value.toString())
                imageArray.add(dataSnapshot.child("imageList").child("2").value.toString())
                for (i in imageArray.indices) {
                    if (imageArray[i] != "") {
                        imageList.add(SlideModel(imageArray[i]))
                    }
                }

                imageSlider.setImageList(imageList)
                var sold = dataSnapshot.child("sold").value.toString()
                if (sold == "false") {
                    sold = "Available"
                    binding.status.text = sold
                } else {
                    sold = "Sold"
                    binding.status.text = sold
                }
                binding.productName.text = dataSnapshot.child("productName").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                binding.productCondition.text = dataSnapshot.child("condition").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.descriptionHeading.text = dataSnapshot.child("productDesc").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                var category = dataSnapshot.child("category").value.toString()
                val addlCat = dataSnapshot.child("additionalCategory").value.toString()
                if (addlCat != "null") {
                    category = "$category($addlCat)"
                }
                binding.productCategory.text = category
                val date = "Selling Date: " + dataSnapshot.child("sellingDate").value.toString()
                binding.statusDate.text = date
                val price = "Rs. " + dataSnapshot.child("price").value.toString()
                binding.productPrice.text = price
                var useTime = dataSnapshot.child("usedForTime").value.toString()
                if (useTime == "") {
                    useTime = "-"
                }
                binding.productUsedFor.text = useTime
                val uid = dataSnapshot.child("userUID").value
                fillUser(uid)


            }
        }.addOnFailureListener{
            TODO("Not yet implemented")
        }

    }

    private fun fillUser(uid: Any?) {
        val databaseUser = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
        databaseUser.child(uid.toString()).get().addOnSuccessListener { snapshot ->

            if (snapshot.exists()){
                val email = snapshot.child("email").value.toString()
                val phone=snapshot.child("phonenum").value.toString()
                val name=snapshot.child("name").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.sellerName.text=name
                binding.sellerEmail.text=email
                binding.sellerPhone.text=phone
                phoneSeller=phone
                emailSeller=email
            }
        }.addOnFailureListener{
            TODO("Not yet implemented")
        }
    }



}