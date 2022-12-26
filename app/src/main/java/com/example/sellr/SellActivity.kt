@file:Suppress("DEPRECATION")

package com.example.sellr

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sellr.data.SellData
import com.example.sellr.utils.CheckInternet
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class SellActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private var userUID:String?=""
    private var emailID:String?=""

    //For SellData Class
    private var imagePrimary : String? =""
    private var imageSecond : String? = ""
    private var imageThird : String? = ""


    private var imageButtonPrimary: ImageButton? =null
    private var imageButtonSecond: ImageButton? =null
    private var imageButtonThird: ImageButton? =null
    private var progressCircular: ProgressBar? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        val user = Firebase.auth.currentUser
        if(user!=null) {
            emailID = user.email
            userUID = user.uid
        }
        else
        {
            makeToast("Not logged in")
            onBackPressed()
        }
        populateDropDown()
        progressCircular=findViewById(R.id.progress_circular)
        //get images from storage on user click
        imageButtonPrimary=findViewById(R.id.imageButtonPrimary)
        imageButtonSecond=findViewById(R.id.imageButtonSecond)
        imageButtonThird=findViewById(R.id.imageButtonThird)
        imageButtonPrimary?.setOnClickListener{

            if(checkInternet())
            {
                val iGallery=Intent(Intent.ACTION_PICK)
                iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                startActivityForResult(iGallery,1000)
            }

        }

        imageButtonSecond?.setOnClickListener{

            if(checkInternet())
            {
                val iGallery=Intent(Intent.ACTION_PICK)
                iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                startActivityForResult(iGallery,2000)
            }


        }
        imageButtonThird?.setOnClickListener{

            if(checkInternet())
            {
                val iGallery=Intent(Intent.ACTION_PICK)
                iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                startActivityForResult(iGallery,3000)

            }

        }



        //On FAB click==data upload
        val button:ExtendedFloatingActionButton=findViewById(R.id.fab)
        button.setOnClickListener{
            if(checkInternet())
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Are you sure?")
                builder.setMessage("Your item will be available publicly for buying")
                builder.setPositiveButton("Yes") { _, _ ->
                    setData()

                }
                builder.setNegativeButton("No") { _, _ ->
                }
                builder.show()
            }
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
            }
        }
    }
    private fun upDateImage(i: Int, data: Intent?) {
        setProgressBar()
        val storageRef = Firebase.storage.reference
        val imageUri: Uri? = data?.data

        val filename = generateUID(emailID+i.toString())
        val uploadTask = storageRef.child("file/$filename").putFile(imageUri!!)
        Handler(Looper.getMainLooper()).postDelayed({
            if(progressCircular?.visibility==View.VISIBLE)
            {
                Toast.makeText(this,"Time out!!Check Your connection",Toast.LENGTH_LONG).show()
                deleteProgressBar()
                uploadTask.cancel()
            }

        }, 300000)
        uploadTask.addOnSuccessListener {
            storageRef.child("file/$filename").downloadUrl.addOnSuccessListener {
                when (i) {
                    1 -> {
                        if(imagePrimary!="")
                        {
                            val ref=Firebase.storage.getReferenceFromUrl(imagePrimary!!)
                            ref.delete()
                        }
                        imageButtonPrimary?.setImageURI(imageUri)
                        imagePrimary=it.toString()

                    }
                    2 -> {
                        if(imageSecond!="")
                        {
                            val ref=Firebase.storage.getReferenceFromUrl(imageSecond!!)
                            ref.delete()
                        }
                        imageButtonSecond?.setImageURI(imageUri)
                        imageSecond=it.toString()
                    }
                    3 -> {
                        imageButtonThird?.setImageURI(imageUri)
                        if(imageThird!="")
                        {
                            val ref=Firebase.storage.getReferenceFromUrl(imageThird!!)
                            ref.delete()
                        }
                        imageThird=it.toString()
                    }
                }
                deleteProgressBar()
            }
        }.addOnFailureListener {
            makeToast("Upload Failed")
            deleteProgressBar()
        }

    }

    private fun setProgressBar(){
        progressCircular?.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }
    private fun deleteProgressBar(){
        progressCircular?.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(progressCircular?.visibility!=0)
        {
            super.onBackPressed()

        }

    }
    private fun setData(){
        setProgressBar()
        val uID: String = generateUID(emailID!!)
        val dataObject=getData(uID)
        if(dataObject==null)
        {
            deleteProgressBar()
            return
        }
        database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")
        val uploadData=database.child(uID).setValue(dataObject)
        Handler(Looper.getMainLooper()).postDelayed({
            if(progressCircular?.visibility==View.VISIBLE)
            {
                Toast.makeText(this,"Listing Failed, Please try again",Toast.LENGTH_LONG).show()
                deleteProgressBar()
                FirebaseDatabase.getInstance().purgeOutstandingWrites()
            }

        }, 180000)
        uploadData.addOnSuccessListener {
            database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
            val upDateUserList=database.child(userUID!!).child("pId").push().setValue(uID)
            upDateUserList.addOnSuccessListener {

                makeToast( "Successfully Listed")
                deleteProgressBar()
                onBackPressed()
                finish()
            }.addOnFailureListener {
                makeToast("Listing Failed, Please try again")
                deleteProgressBar()
            }
        }.addOnFailureListener {
            makeToast("Listing Failed, Please try again")
            deleteProgressBar()
        }

    }
    private fun getData(uID:String): SellData? {
        var flag=true
        val productName=findViewById<EditText>(R.id.textFieldName).text.toString().trim()
        if(productName=="")
        {
            findViewById<TextInputLayout>(R.id.inputName).error="This field is required"
            flag=false
        }
        else
        {
            findViewById<TextInputLayout>(R.id.inputName).error=null
        }
        val category=findViewById<AutoCompleteTextView>(R.id.categoryDropDown).text.toString()
        if(category=="Choose a Category")
        {
            findViewById<TextInputLayout>(R.id.inputCategory).error="This field is required"
            flag=false
        }
        else
        {
            findViewById<TextInputLayout>(R.id.inputCategory).error=null
        }
        val productDesc=findViewById<EditText>(R.id.textFieldDesc).text.toString().trim()
        if(productDesc=="")
        {
            findViewById<TextInputLayout>(R.id.inputDesc).error="This field is required"
            flag=false
        }
        else
        {
            findViewById<TextInputLayout>(R.id.inputDesc).error=null
        }
        val parentChipGroup:ChipGroup=findViewById(R.id.chipGroup)
        var condition: String?=null
        if(parentChipGroup.checkedChipId!=View.NO_ID){
            findViewById<TextView>(R.id.chipError).visibility=View.GONE
            condition= parentChipGroup.findViewById<Chip>(parentChipGroup.checkedChipId).text.toString()
        }
        else
        {
            findViewById<TextView>(R.id.chipError).visibility=View.VISIBLE
            flag=false
        }

        val price=findViewById<EditText>(R.id.price).text.toString()
        if(price=="")
        {
            findViewById<TextInputLayout>(R.id.inputPrice).error="This field is required"
            flag=false
        }
        else
        {
            findViewById<TextInputLayout>(R.id.inputPrice).error=null
        }
        if(imagePrimary==""||imageSecond==""||imageThird=="")
        {
            findViewById<TextView>(R.id.imageError).visibility=View.VISIBLE
            flag=false

        }
        else
        {
            findViewById<TextView>(R.id.imageError).visibility=View.GONE
        }
        if(flag) {
            return SellData(
                productName,
                productDesc,
                category,
                condition,
                price,
                imagePrimary,
                imageSecond,
                imageThird,
                userUID,false,uID
            )
        }
        else
        {
            return null
        }


    }
    private fun checkInternet():Boolean{
        if (CheckInternet.isConnectedToInternet(applicationContext)) {
            Toast.makeText(
                applicationContext, "Something went wrong! Check your network...",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
    private fun makeToast(value:String){

        Toast.makeText(applicationContext,value,Toast.LENGTH_LONG).show()
    }
    @SuppressLint("SimpleDateFormat")
    private fun generateUID(emailID:String):String
    {

        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        val datetime: String = ft.format(dNow)
        return emailID.substringBeforeLast("@")+datetime
    }
    private fun populateDropDown()
    {
        //Populate dropDown category list
        val categories = resources.getStringArray(R.array.Categories)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu_category_item, categories)
        findViewById<AutoCompleteTextView>(R.id.categoryDropDown).setAdapter(arrayAdapter)
    }
    override fun onRestart() {
        super.onRestart()
        populateDropDown()
    }

    override fun onResume() {
        super.onResume()
        populateDropDown()
    }


}