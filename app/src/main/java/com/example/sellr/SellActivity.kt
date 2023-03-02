@file:Suppress("DEPRECATION")

package com.example.sellr

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.sellr.data.SellData
import com.example.sellr.databinding.ActivitySellBinding
import com.example.sellr.utils.CheckInternet
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SellActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var userUID: String? = ""
    private var emailID: String? = ""

    //For SellData Class
    private var imagePrimary: String? = ""
    private var imageArray = ArrayList<String>()
    private var imageButtonPrimary: ImageButton? = null
    private var imageButtonSecond: ImageButton? = null
    private var imageButtonThird: ImageButton? = null
    private var imageButtonFourth: ImageButton? = null
    private var progressCircular: ProgressBar? = null


    private lateinit var baos:ByteArrayOutputStream
    private lateinit var uploadTask:UploadTask
    //private val coreHelper = AnstronCoreHelper(this)
    lateinit var binding : ActivitySellBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        imageArray.add("")
        imageArray.add("")
        imageArray.add("")

        //autologin feature
        val user = Firebase.auth.currentUser
        if (user != null) {
            emailID = user.email
            userUID = user.uid
        } else {
            makeToast("Not logged in")
            onBackPressed()
        }

        populateDropDown()
        binding.categoryDropDown.doAfterTextChanged {
            if (binding.categoryDropDown.text.toString() == "Others") {
                showDialog()
            } else {
                binding.inputCategory.helperText = null
            }
        }
        progressCircular = binding.progressCircular
        //get images from storage on user click
        //until the previous image is selected, the next one is not enabled
        imageButtonPrimary = binding.imageButtonFirst
        imageButtonSecond =  binding.imageButtonSecond
        imageButtonThird =  binding.imageButtonThird
        imageButtonFourth =  binding.imageButtonFourth
        imageButtonSecond?.isEnabled = false
        imageButtonThird?.isEnabled = false
        imageButtonFourth?.isEnabled = false


        //picking the image from the gallery and sending an intent

        //as soon as a image is selected by the picker, and intent is passed to identify which image is to be
        //updated and then it is directly uploaded in the updateImage method
        //then when final list it is clicked it is linked (FAB onClickListener)

        //the request code is used to identify which image in the array is to be updated
        imageButtonPrimary?.setOnClickListener {
            if (checkInternet()) {
                //________________________________________________________________________________
//                Previous Codes
//val iGallery = Intent(Intent.ACTION_PICK)
//                iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                    startActivityForResult(iGallery, 1000)
                //_______________________________________________________________________________
                ImagePicker.with(this).crop().
                compress(250).
                maxResultSize(600,600)
                    .start(1000)
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


        //On used click
        binding.usedCondition.isEnabled = false
        val usedChip: Chip = binding.usedChip
        val newChip: Chip =  binding.newChip
        usedChip.setOnClickListener {
            chipClicked()
        }
        newChip.setOnClickListener {
            chipClicked()
        }


        //On FAB click==data upload
        //final data upload
        val button: ExtendedFloatingActionButton =  binding.fab
        button.setOnClickListener {
            if (checkInternet()) {
                setProgressBar()
                val dtb =
                    FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
                dtb.child("Users").child(Firebase.auth.uid.toString()).get()
                    .addOnSuccessListener {
                        val check = it.child("infoentered").toString()

                        if (check.contains("no")) {
                            Toast.makeText(
                                applicationContext,
                                "Please complete your profile info before selling an item",
                                Toast.LENGTH_LONG
                            ).show()
                            deleteProgressBar()
                            val i = Intent(applicationContext, MainFragmentHolder::class.java)
                            i.putExtra("extraDetails", "extraDetails")
                            startActivity(i)


                        } else {

                            deleteProgressBar()
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

                    }.addOnFailureListener {
                        deleteProgressBar()

                    }

            }
        }
    }

//    private fun compressImage(pickedImageURI: Uri): Uri{
//        val filePath = File(SiliCompressor.with(this).compress(pickedImageURI.toString(), File(this.cacheDir, "temp")))
//        return Uri.fromFile(filePath)
//}

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

    private fun chipClicked() {
        val usedClicked = binding.usedChip
        binding.usedCondition.isEnabled = usedClicked.isChecked
    }

    //updating image in firebase storage
    private fun upDateImage(i: Int, data: Intent?) {
        setProgressBar()
        val storageRef = Firebase.storage.reference

        val imageUri: Uri? = data?.data

        val filename = generateUID(emailID + i.toString())


//___________________________________________________________________________________________________________________
//          Failed compressor trials
//        //uploading image in firebase storage
//        // val sendUri = compressImage(imageUri!!)
//
////        SiliCompressor.with
////            (this).compress(imageUri.toString(),
////            File(this.cacheDir, "temp"))
//        val filePath = SiliCompressor.with(this@SellActivity).compress(imageUri.toString(),this.cacheDir)
//
//       val sendUri = Uri.parse(filePath)
//        val bmp :Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
//        bmp.compress(Bitmap.CompressFormat.JPEG,25,baos)
//        val bytedata = baos.toByteArray()
//        uploadTask=storageRef.child("file/$filename").child(coreHelper.getFileNameFromUri(sendUri)).putFile(sendUri)
//___________________________________________________________________________________________________________________

        uploadTask = storageRef.child("file/$filename").putFile(imageUri!!)


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
            storageRef.child("file/$filename").downloadUrl.addOnSuccessListener {
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (progressCircular?.visibility != 0) {
            super.onBackPressed()

        }

    }

    //this method gets the data about the item and sets it in the firebase
    private fun setData() {
        setProgressBar()
        val uID: String = generateUID(emailID!!)
        val dataObject = getData(uID)
        if (dataObject == null) {
            deleteProgressBar()
            return
        }
        database =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Items")
        val uploadData = database.child(uID).setValue(dataObject)
        Handler(Looper.getMainLooper()).postDelayed({
            if (progressCircular?.visibility == View.VISIBLE) {
                Toast.makeText(this, "Listing Failed, Please try again", Toast.LENGTH_LONG).show()
                deleteProgressBar()
                FirebaseDatabase.getInstance().purgeOutstandingWrites()
            }

        }, 180000)
        uploadData.addOnSuccessListener {
            database =
                FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Users")
            val upDateUserList = database.child(userUID!!).child("pId").push().setValue(uID)
            upDateUserList.addOnSuccessListener {
                makeToast("Successfully Listed")
                deleteProgressBar()
                closeKeyboard()
                binding.sellMainScrollView.visibility=View.GONE
                binding.successAnimationView.visibility=View.VISIBLE
                binding.fab.visibility=View.INVISIBLE
                supportActionBar?.hide()
                binding.successAnimationView.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({

                    onBackPressed()
                    finish()
                }, 2000)

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
    //used to get the data from the menu and send an object containing all the information
    //to the setData() method
    private fun getData(uID: String): SellData? {
        var flag = true
        val productName =  binding.textFieldName.text.toString().trim()
        if (productName == "") {
            binding.inputName.error = "This field is required"

            flag = false
        } else {
            binding.inputName.error = null
        }
        val category =  binding.categoryDropDown.text.toString()
        val ipCategory =  binding.inputCategory
        val additionalCategory = ipCategory.helperText.toString()
        if (category == "") {
            ipCategory.error = "This field is required"
            flag = false
        } else if (category == "Others") {
            if (additionalCategory == "null") {
                ipCategory.error = "Please Specify Item Category"
                flag = false
            }
        } else {
            ipCategory.error = null
        }
        val productDesc = binding.textFieldDesc.text.toString().trim()
        if (productDesc == "") {
            binding.inputDesc.error = "This field is required"
            flag = false
        } else {
            binding.inputDesc.error = null
        }
        val parentChipGroup: ChipGroup =  binding.chipGroup
        var condition: String? = null
        if (parentChipGroup.checkedChipId != View.NO_ID) {
            binding.chipError.visibility = View.GONE
            condition =
                parentChipGroup.findViewById<Chip>(parentChipGroup.checkedChipId).text.toString()
        } else {
            binding.chipError.visibility = View.VISIBLE
            flag = false
        }

        val price = binding.price.text.toString()
        if (price == "") {
            binding.inputPrice.error = "This field is required"
            flag = false
        } else {
            binding.inputPrice.error = null
        }


        if (imagePrimary == "") {
            binding.imageError.visibility = View.VISIBLE
            flag = false

        } else {
            binding.imageError.visibility = View.GONE
        }


        var usedTime = ""
        if (condition == "Used") {
            usedTime =  binding.textFieldUsedCondition.text.toString()
            if (usedTime == "") {
                binding.usedCondition.error = "This field is required"
                flag = false
            } else {
                binding.usedCondition.error = null
            }
        }

        if (flag) {
            val currentDate = SimpleDateFormat("dd-MM-yyyy").format(Date())


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
                userUID, false, uID, currentDate
            )
        } else {
            return null
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
            val update = binding.inputCategory;
            update.error = null
            update.helperText = customCat
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp

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

    private fun populateDropDown() {
        //Populate dropDown category list
        val categories = resources.getStringArray(R.array.Categories)
        val categoriesAdapter = ArrayAdapter(this, R.layout.dropdown_menu_category_item, categories)
        binding.categoryDropDown.setAdapter(categoriesAdapter)
        val conditions = resources.getStringArray(R.array.Conditions)
        val conditionsAdapter = ArrayAdapter(this, R.layout.dropdown_menu_category_item, conditions)
        binding.textFieldUsedCondition.setAdapter(conditionsAdapter)

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
    override fun onRestart() {
        super.onRestart()
        populateDropDown()
    }

    override fun onResume() {
        super.onResume()
        populateDropDown()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}