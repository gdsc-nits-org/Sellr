package com.example.sellr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sellr.databinding.ActivityDescrptionPageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase


class DescriptionPage : AppCompatActivity() {
    private lateinit var binding: ActivityDescrptionPageBinding
    private lateinit var imageList: ArrayList<SlideModel>
    private lateinit var imageSlider: ImageSlider
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
        binding.floatingActionButton2.setOnClickListener{
            bottomSheetFunction(binding.sellerPhone.text.toString(),binding.sellerEmail.text.toString())
        }
    }
    private fun fetchDataFromDataBase(items:String)
    {
        val databaseItem = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")
        databaseItem.child(items).get().addOnSuccessListener {

            if (it.exists()){
                val primaryImage=it.child("imagePrimary").value.toString()
                imageList.add(SlideModel(primaryImage))
                val imageArray=ArrayList<String>()
                imageArray.add(it.child("imageList").child("0").value.toString())
                imageArray.add(it.child("imageList").child("1").value.toString())
                imageArray.add(it.child("imageList").child("2").value.toString())
                for (i in imageArray.indices) {
                    if(imageArray[i]!="")
                    {
                        imageList.add(SlideModel(imageArray[i]))
                    }
                    else
                    {
                        imageList.add(SlideModel(primaryImage))
                    }
                }

                imageSlider.setImageList(imageList)
                var sold=it.child("sold").value.toString()
                if(sold=="false")
                {
                    sold="Available"
                    binding.status.text=sold
                }
                else
                {
                    sold="Sold"
                    binding.status.text=sold
                }
                binding.productName.text=it.child("productName").value.toString()
                binding.productCondition.text=it.child("condition").value.toString()
                binding.descriptionHeading.text=it.child("productDesc").value.toString()
                binding.productCategory.text=it.child("category").value.toString()
                binding.statusDate.text=it.child("sellingDate").value.toString()
                binding.productPrice.text=it.child("price").value.toString()
                val uid=it.child("userUID").value
                fillUser(uid)



            }
        }.addOnFailureListener{
            TODO("Not yet implemented")
        }

    }

    private fun fillUser(uid: Any?) {
        val databaseUser = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
        databaseUser.child(uid.toString()).get().addOnSuccessListener {

            if (it.exists()){
                val email = it.child("email").value.toString()
                val phone=it.child("phonenum").value.toString()
                val name=it.child("name").value.toString()
                binding.sellerName.text=name
                binding.sellerEmail.text=email
                binding.sellerPhone.text=phone
            }
        }.addOnFailureListener{
            TODO("Not yet implemented")
        }
    }
    private fun bottomSheetFunction(
        phoneNo: String?,
        emailID: String?,
    ) {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.fragment_bottom_sheet)
        val email: ImageView? = dialog.findViewById(R.id.email_icon)
        val phone: ImageView? = dialog.findViewById(R.id.phone_icon)
        email?.setOnClickListener {
            emailID?.let { val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:$it")
                startActivity(intent) }
                ?: Toast.makeText(applicationContext, "Not Available", Toast.LENGTH_SHORT).show()
        }
        phone?.setOnClickListener{
            phoneNo?.let {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$it")
                startActivity(intent)
            }
                ?: Toast.makeText(applicationContext, "Not Available", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }



}