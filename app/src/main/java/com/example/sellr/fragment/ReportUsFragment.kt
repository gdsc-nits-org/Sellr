package com.example.sellr.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sellr.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class ReportUsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_report_us, container, false)
        view.findViewById<Button>(R.id.button_submit_feedback).setOnClickListener {

        }
        view.findViewById<ImageButton>(R.id.imageView_feedback).setOnClickListener {
            val iGallery=Intent(Intent.ACTION_PICK)
            iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(iGallery,1000)
        }
        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                1000 -> {
//                    upDateImage(1, data)
                    val storageRef = Firebase.storage.reference
                    val imageUri: Uri? = data?.data

                    val dNow = Date()
                    val ft = SimpleDateFormat("yyMMddhhmmssMs")
                    val filename: String = ft.format(dNow)
                    val uploadTask = storageRef.child("file/$filename").putFile(imageUri!!)
                    uploadTask.addOnSuccessListener {
                        storageRef.child("file/$filename").downloadUrl.addOnSuccessListener {

//                            when (i) {
//                                1 -> {
//                                    if(imagePrimary!="")
//                                    {
//                                        val ref= Firebase.storage.getReferenceFromUrl(imagePrimary!!)
//                                        ref.delete()
//                                    }
//                                    imageButtonPrimary?.setImageURI(imageUri)
//                                    imagePrimary=it.toString()
//                                    imageButtonSecond?.isEnabled=true
//                                    imageButtonSecond?.visibility= View.VISIBLE
//                                    imageButtonSecond?.setImageResource(R.drawable.ic_image_placeholder)
//
//                                }
//                                2 -> {
//                                    if(imageArray[0]!="")
//                                    {
//                                        val ref= Firebase.storage.getReferenceFromUrl(imageArray[0])
//                                        ref.delete()
//                                    }
//                                    imageButtonSecond?.setImageURI(imageUri)
//                                    imageArray[0]=it.toString()
//                                    imageButtonThird?.isEnabled=true
//                                    imageButtonThird?.visibility= View.VISIBLE
//                                    imageButtonThird?.setImageResource(R.drawable.ic_image_placeholder)
//                                }
//                                3 -> {
//                                    imageButtonThird?.setImageURI(imageUri)
//                                    if(imageArray[1]!="")
//                                    {
//                                        val ref= Firebase.storage.getReferenceFromUrl(imageArray[2])
//                                        ref.delete()
//                                    }
//                                    imageArray[1]=it.toString()
//                                    imageButtonFourth?.isEnabled=true
//                                    imageButtonFourth?.visibility= View.VISIBLE
//                                    imageButtonFourth?.setImageResource(R.drawable.ic_image_placeholder)
//                                }
//                                4 -> {
//                                    imageButtonFourth?.setImageURI(imageUri)
//                                    if(imageArray[2]!="")
//                                    {
//                                        val ref= Firebase.storage.getReferenceFromUrl(imageArray[2])
//                                        ref.delete()
//                                    }
//                                    imageArray[2]=it.toString()
//                                }
//                            }
//                            deleteProgressBar()
                        }
                    }.addOnFailureListener {
//                        makeToast("Upload Failed")
//                        deleteProgressBar()
                    }


                }

            }
        }
    }

}