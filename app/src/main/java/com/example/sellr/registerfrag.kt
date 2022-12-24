package com.example.sellr.fragment

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sellr.AuthActivity
import com.example.sellr.R
import com.example.sellr.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private lateinit var email:EditText
    private lateinit var pass:EditText
    private lateinit var confrmpass:EditText
    private lateinit var signupbtn:Button
    private lateinit var auth:FirebaseAuth
     private lateinit var emailtxt:String
    private lateinit var passtxt:String
    private lateinit var user:FirebaseUser
    private lateinit var database:DatabaseReference

    lateinit var register: TextView
    lateinit var actionbar: ActionBar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_registerfrag, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
         database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        email = view.findViewById(R.id.editTextEmail)
        pass = view.findViewById(R.id.editTextTextPassword)
        confrmpass = view.findViewById(R.id.editTextTextPassword2)
        signupbtn = view.findViewById(R.id.button)




        signupbtn.setOnClickListener {
            emailtxt = email.text.toString().trim()
            passtxt = pass.text.toString().trim()
            auth.createUserWithEmailAndPassword(emailtxt, passtxt)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {

                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(requireContext(),"Verification Link Sent, Please Verify",Toast.LENGTH_LONG).show()
                            user = auth.currentUser!!
                            val uid = user.uid.toString()
                            saveuserinfo(emailtxt,passtxt,uid)
                            updateUI(user)

                        }
                        // Sign in success, update UI with the signed-in user's information


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(requireContext(),"Sign Up Unsuccessful",Toast.LENGTH_LONG).show()
                      //  updateUI(null)
                    }
                }

        }





        return view

    }

    private fun saveuserinfo(emailtxt: String, passtxt: String, uid: String) {

        val user = UserModel(emailtxt,passtxt)
        database.child("Users").child(uid).setValue(user)

    }

    private fun updateUI(user: FirebaseUser?) {

        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)


    }



}