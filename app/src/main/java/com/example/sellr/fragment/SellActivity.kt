@file:Suppress("DEPRECATION")

package com.example.sellr

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
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
import kotlin.collections.ArrayList


class SellActivity : AppCompatActivity(){
    private lateinit var database : DatabaseReference
    private var userUID:String?=""
    private var emailID:String?=""

    //For SellData Class
    private var imagePrimary : String? = ""
    private var imageArray = ArrayList<String>()
    private var imageButtonPrimary: ImageButton? =null
    private var imageButtonSecond: ImageButton? =null
    private var imageButtonThird: ImageButton? =null
    private var imageButtonFourth: ImageButton? =null
    private var progressCircular: ProgressBar? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        imageArray.add("")
        imageArray.add("")
        imageArray.add("")
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
        findViewById<AutoCompleteTextView>(R.id.categoryDropDown).doAfterTextChanged {
            if(findViewById<AutoCompleteTextView>(R.id.categoryDropDown).text.toString()=="Others")
            {
                showDialog()
            }
            else
            {
                findViewById<TextInputLayout>(R.id.inputCategory).helperText=null
            }
        }


        progressCircular=findViewById(R.id.progress_circular)
        //get images from storage on user click
        imageButtonPrimary=findViewById(R.id.imageButtonFirst)
        imageButtonSecond=findViewById(R.id.imageButtonSecond)
        imageButtonThird=findViewById(R.id.imageButtonThird)
        imageButtonFourth=findViewById(R.id.imageButtonFourth)
        imageButtonSecond?.isEnabled=false
        imageButtonThird?.isEnabled=false
        imageButtonFourth?.isEnabled=false
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
        imageButtonFourth?.setOnClickListener{

            if(checkInternet())
            {
                val iGallery=Intent(Intent.ACTION_PICK)
                iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                startActivityForResult(iGallery,4000)
            }

        }

        //On used click
        findViewById<TextInputLayout>(R.id.usedCondition).isEnabled=false
        val usedChip:Chip=findViewById(R.id.usedChip)
        val newChip:Chip=findViewById(R.id.newChip)
        usedChip.setOnClickListener{
            chipClicked()
        }
        newChip.setOnClickListener{
            chipClicked()
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
                4000 -> {
                    upDateImage(4, data)
                }
            }
        }
    }
    private fun chipClicked()
    {
        val usedClicked=findViewById<Chip>(R.id.usedChip)
        findViewById<TextInputLayout>(R.id.usedCondition).isEnabled = usedClicked.isChecked
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
                        imageButtonSecond?.isEnabled=true
                        imageButtonSecond?.visibility= View.VISIBLE
                        imageButtonSecond?.setImageResource(R.drawable.ic_image_placeholder)

                    }
                    2 -> {
                        if(imageArray[0]!="")
                        {
                            val ref=Firebase.storage.getReferenceFromUrl(imageArray[0])
                            ref.delete()
                        }
                        imageButtonSecond?.setImageURI(imageUri)
                        imageArray[0]=it.toString()
                        imageButtonThird?.isEnabled=true
                        imageButtonThird?.visibility= View.VISIBLE
                        imageButtonThird?.setImageResource(R.drawable.ic_image_placeholder)
                    }
                    3 -> {
                        imageButtonThird?.setImageURI(imageUri)
                        if(imageArray[1]!="")
                        {
                            val ref=Firebase.storage.getReferenceFromUrl(imageArray[2])
                            ref.delete()
                        }
                        imageArray[1]=it.toString()
                        imageButtonFourth?.isEnabled=true
                        imageButtonFourth?.visibility= View.VISIBLE
                        imageButtonFourth?.setImageResource(R.drawable.ic_image_placeholder)
                    }
                    4 -> {
                        imageButtonFourth?.setImageURI(imageUri)
                        if(imageArray[2]!="")
                        {
                            val ref=Firebase.storage.getReferenceFromUrl(imageArray[2])
                            ref.delete()
                        }
                        imageArray[2]=it.toString()
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
    @SuppressLint("SimpleDateFormat")
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
        val ipCategory=findViewById<TextInputLayout>(R.id.inputCategory)
        val additionalCategory=ipCategory.helperText.toString()
        if(category=="")
        {
            ipCategory.error="This field is required"
            flag=false
        }
        else if(category=="Others")
        {
            if(additionalCategory=="null")
            {
                ipCategory.error="Please Specify Item Category"
                flag=false
            }
        }
        else
        {
            ipCategory.error=null
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
        if(imagePrimary=="")
        {
            findViewById<TextView>(R.id.imageError).visibility=View.VISIBLE
            flag=false

        }
        else
        {
            findViewById<TextView>(R.id.imageError).visibility=View.GONE
        }
        var usedTime=""
        if(condition=="Used")
        {
            usedTime=findViewById<EditText>(R.id.textFieldUsedCondition).text.toString()
            if(usedTime=="")
            {
                findViewById<TextInputLayout>(R.id.usedCondition).error="This field is required"
                flag=false
            }
            else
            {
                findViewById<TextInputLayout>(R.id.usedCondition).error=null
            }
        }

        if(flag) {
            val currentDate=SimpleDateFormat("dd-MM-yyyy").format(Date())


            return SellData(
                productName,
                productDesc,
                category,
                additionalCategory,
                condition,
                usedTime,
                price,
                imagePrimary,
                imageArray,
                userUID, false, uID,currentDate
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
    private fun showDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.other_category)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val etPost = dialog.findViewById<EditText>(R.id.et_post)
        dialog.findViewById<View>(R.id.bt_cancel)
            .setOnClickListener { dialog.dismiss() }
        dialog.findViewById<View>(R.id.btn_submit).setOnClickListener { _: View? ->
            val customCat = etPost.text.toString().trim { it <= ' ' }
            val update=findViewById<TextInputLayout>(R.id.inputCategory)
            update.error=null
            update.helperText=customCat
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp

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
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu_category_item,categories)
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