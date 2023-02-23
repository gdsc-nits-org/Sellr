package com.example.sellr

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sellr.databinding.ActivityLostAndFoundDescriptionPageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class LostAndFoundDescriptionPage : AppCompatActivity() {
    private lateinit var binding: ActivityLostAndFoundDescriptionPageBinding
    private lateinit var imageList: ArrayList<SlideModel>
    private lateinit var imageSlider: ImageSlider
    private lateinit var backDrop: View
    private lateinit var lytMic: View
    private lateinit var lytCall: View
    private var rotate = false

    private var emailUser = ""
    private var phoneUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLostAndFoundDescriptionPageBinding.inflate(layoutInflater)
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

        imageSlider = findViewById(R.id.image_slider)
        imageList = ArrayList()



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
            intent.data = Uri.parse("mailto:$emailUser")
            startActivity(intent)
        }

        lytCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneUser")
            startActivity(intent)
        }
        fabMic.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailUser")
            startActivity(intent)
        }

        fabCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneUser")
            startActivity(intent)
        }


    }


    private fun toggleFabMode(v: View) {
        rotate = !rotate
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


    private fun fetchDataFromDataBase(items: String) {
        val databaseItem =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("LostAndFound")
        databaseItem.child(items).get().addOnSuccessListener { dataSnapshot ->

            if (dataSnapshot.exists()) {
                val primaryImage = dataSnapshot.child("imagePrimary").value.toString()
                val Image0 = dataSnapshot.child("imageList").child("0").value.toString()
                val Image1 = dataSnapshot.child("imageList").child("1").value.toString()
                val Image2 = dataSnapshot.child("imageList").child("2").value.toString()
                if (primaryImage == "") {
                    imageList.add(SlideModel(R.drawable.no_image))
                } else {
                    imageList.add(SlideModel(primaryImage))
                    val imageArray = ArrayList<String>()

                    imageArray.add(Image0)
                    imageArray.add(Image1)
                    imageArray.add(Image2)
                    for (i in imageArray.indices) {
                        if (imageArray[i] != "") {
                            imageList.add(SlideModel(imageArray[i]))
                        }
                    }
                }
                val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
                imageSlider.setImageList(imageList)
                imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
                        val intent = Intent(applicationContext, Zooming::class.java).also {
                            it.putExtra("PrimaryImage", primaryImage)
                            it.putExtra("Image0", Image0)
                            it.putExtra("Image1", Image1)
                            it.putExtra("Image2", Image2)
                        }
                        startActivity(intent)
                    }
                })

                binding.objectName.text = dataSnapshot.child("objectName").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                val category = dataSnapshot.child("lostOrFound").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.productCategory.text = category
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.description.text = dataSnapshot.child("objectDescription").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }


                binding.objectLocation.text = dataSnapshot.child("objectLocation").value.toString()

                val uid = dataSnapshot.child("uid").value
                fillUser(uid)


            }
        }.addOnFailureListener {
            TODO("Not yet implemented")
        }

    }

    private fun fillUser(uid: Any?) {
        val databaseUser =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users")
        databaseUser.child(uid.toString()).get().addOnSuccessListener { snapshot ->

            if (snapshot.exists()) {
                val email = snapshot.child("email").value.toString()
                val phone = snapshot.child("phonenum").value.toString()
                val name = snapshot.child("name").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.userName.text = name
                binding.userEmail.text = email
                binding.userPhone.text = phone
                phoneUser = phone
                emailUser = email
            }
        }.addOnFailureListener {
            TODO("Not yet implemented")
        }
    }
}
