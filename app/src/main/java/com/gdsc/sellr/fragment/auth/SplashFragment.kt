package com.gdsc.sellr.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gdsc.sellr.MainActivity
import com.gdsc.sellr.R
import com.gdsc.sellr.utils.CheckInternet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var dtb: DatabaseReference
private lateinit var auth: FirebaseAuth

class SplashFragment : Fragment() {

    private var animOver=false
    private var loadingOver=false
    private var goToMainScreen=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            animOver=true
            if(loadingOver)
            {
                endSplash()
            }
            else
            {
                view?.findViewById<TextView>(R.id.appNameSplash)?.visibility=View.INVISIBLE
                view?.findViewById<TextView>(R.id.gdscNameSplash)?.visibility=View.INVISIBLE
                view?.findViewById<ProgressBar>(R.id.progressBarSplash)?.visibility=View.VISIBLE
            }
            checkForDetails()


        }, 4000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun endSplash() {
        if(!goToMainScreen) {
            fragmentload(LoginFragment())
        }
        else
        {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun checkForDetails():Boolean {
        if(context?.let { CheckInternet.isConnectedToInternet(it) } == true)
        {
            val contextView=view?.findViewById<View>(R.id.splashContainer)
            if (contextView != null) {
                Snackbar.make(contextView, "No Internet Available", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry") {
                        // Responds to click on the action
                        checkForDetails()
                    }
                    .show()
            }
            return false
        }
        val user = FirebaseAuth.getInstance().currentUser
        dtb = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()

        if ( user!=null ) {

            println(user.uid.toString())

            dtb.child("Users").child(user.uid.toString()).get().addOnSuccessListener {
                val check = it.child("infoentered").toString();
                if(!check.contains("no")) {
                    goToMainScreen=true
                }
                loadingOver=true
                if(animOver)
                {
                    endSplash()
                }


            }.addOnFailureListener{
                Toast.makeText(context,"Failed to connect to the internet",Toast.LENGTH_SHORT).show()
            }
            // User is signed in


        } else {
            // User is signed out
            fragmentLoad(LoginFragment())
        }
        return true
    }


    private fun fragmentLoad(fragment : Fragment)
    {

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authFrameLayout, fragment)
        fragmentTransaction.commit()

    }

    private fun fragmentload(fragment : Fragment)
    {

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authFrameLayout, fragment)
        fragmentTransaction.commit()

    }
}