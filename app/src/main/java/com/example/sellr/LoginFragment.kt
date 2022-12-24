package com.example.sellr

import android.app.ActionBar
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.sellr.fragment.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    lateinit var register: TextView
    lateinit var actionbar: ActionBar
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var signupbtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var emailtxt:String
    private lateinit var passtxt:String




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        register = view.findViewById(R.id.textViewregister)
        email = view.findViewById(R.id.editTextTextPersonName)
        pass = view.findViewById(R.id.editTextTextPassword)
        signupbtn = view.findViewById(R.id.button)

        auth = FirebaseAuth.getInstance()
        register.setOnClickListener {
            fragmentload(RegisterFragment())


        }
        signupbtn.setOnClickListener {
            emailtxt = email.text.toString().trim()
            passtxt = pass.text.toString().trim()
            auth.signInWithEmailAndPassword(emailtxt, passtxt)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {

                        val isverified = auth.currentUser?.isEmailVerified
                        if(isverified==true)
                        {
                            Toast.makeText(requireContext(), "Authentication Successful.",
                                Toast.LENGTH_SHORT).show()
                            val user = Firebase.auth.currentUser
                            println("UID is "+ user?.uid.toString())
                            updateUI(user)

                        }
                        else
                            Toast.makeText(requireContext(), "Email Not Verified",
                                Toast.LENGTH_SHORT).show()


                    } else {
                        // If sign in fails, display a message to the user.
                        println("Error reason"+ task.exception)
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                       // updateUI(null)
                    }
                }
        }




        return view

    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }

    private fun fragmentload(fragment : Fragment)
    {

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authFrameLayout, fragment)
        fragmentTransaction.commit()

    }






}