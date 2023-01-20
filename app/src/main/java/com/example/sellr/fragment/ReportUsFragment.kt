package com.example.sellr.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import android.widget.ImageView.ScaleType
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sellr.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class ReportUsFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var radioGroup: RadioGroup
    private var radioCategory = "Issues"
    private var imageUri: Uri? = null
    private var progressCircular: ProgressBar? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report_us, container, false)
        radioGroup = view.findViewById(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->

            // on below line we are getting radio button from our group.
            radioCategory = view.findViewById<RadioButton>(checkedId).text.toString()


        }
        view.findViewById<Button>(R.id.button_submit_feedback).setOnClickListener {

        }
        view.findViewById<ImageButton>(R.id.imageButton_feedback).setOnClickListener {
            val iGallery = Intent(Intent.ACTION_PICK)
            iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(iGallery, 1000)
        }
        view.findViewById<Button>(R.id.button_submit_feedback).setOnClickListener {
            progressCircular=view.findViewById(R.id.progress_circular_feedback)
            sendFeedback()
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                1000 -> {
                    imageUri = data?.data
                    view?.findViewById<ImageButton>(R.id.imageButton_feedback)!!
                        .setImageURI(imageUri)
                    view?.findViewById<ImageButton>(R.id.imageButton_feedback)!!
                        .scaleType = ScaleType.FIT_START
                    view?.findViewById<TextView>(R.id.feedback_image_last)?.visibility =
                        View.INVISIBLE
                }
            }
        }
    }

    data class FeedbackData(
        val feedbackDesc: String? = null,
        val feedbackImage: String? = null,
        val feedbackCategory: String? = null,
        val userID:String?=null
    )

    private fun sendFeedback() {
        setProgressBar()
        val desc =
            view?.findViewById<TextInputEditText>(R.id.textField_button_submit_feedback)?.text.toString()
        if (desc != "" && imageUri != null) {
            view?.findViewById<TextInputLayout>(R.id.input_submit_feedback)?.error = null
            val storageRef = Firebase.storage.reference
            val dNow = Date()
            val ft = SimpleDateFormat("yyMMddhhmmssMs")
            val filename: String = ft.format(dNow)
            val uploadTask = storageRef.child("$radioCategory/$filename").putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                storageRef.child("$radioCategory/$filename").downloadUrl.addOnSuccessListener {
                    database =
                        FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("FeedBack")
                    val uploadData = database.child(filename)
                        .setValue(FeedbackData(desc, it.toString(), radioCategory,Firebase.auth.currentUser?.uid.toString()))
                    uploadData.addOnSuccessListener {
                        //Disable progressbar
                        deleteProgressBar()
                        activity?.onBackPressed()
                        Toast.makeText(context, "Feedback Submitted", Toast.LENGTH_LONG)
                            .show()
                    }.addOnFailureListener {
                        deleteProgressBar()
                        Toast.makeText(context, "Failed to submit Feedback", Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }.addOnFailureListener {
                    deleteProgressBar()
            }

        } else {
            deleteProgressBar()
            if (desc == "") {
                view?.findViewById<TextInputLayout>(R.id.input_submit_feedback)?.error =
                    "This field is required"
            }
            else
            {
                view?.findViewById<TextInputLayout>(R.id.input_submit_feedback)?.error =null
            }
            val imageButton = view?.findViewById<TextView>(R.id.feedback_image_last)
            if (imageUri == null) {

                imageButton?.text = "Please Upload a relevant Screenshot"
                imageButton?.setTextColor(Color.parseColor("#B00020"))

            }
            else
            {
                imageButton?.visibility=View.INVISIBLE
            }
        }


    }
    private fun setProgressBar(){
        progressCircular?.visibility = View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }
    private fun deleteProgressBar(){
        progressCircular?.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }


}