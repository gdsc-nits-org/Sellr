package com.example.sellr

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
import com.example.sellr.utils.CheckInternet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import papaya.`in`.sendmail.SendMail


// Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var dtb: DatabaseReference
private lateinit var auth: FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {
    // Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val mail = SendMail(
//            "sam33rzaidi@gmail.com", "nfvshodcoxiwknas",
//            "sameer21_ug@ece.nits.ac.in",
//            "Testing Email Sending",
//            "Yes, it's working well\nI will use it alwayswithotp123."
//        )
//        mail.execute()
//        println("email sent")
        // Token : usingforsellr123 ---- // nfvshodcoxiwknas
        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            checkForDetails()


        }, 4000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
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
        view?.findViewById<TextView>(R.id.appNameSplash)?.visibility=View.INVISIBLE
        view?.findViewById<TextView>(R.id.gdscNameSplash)?.visibility=View.INVISIBLE
        view?.findViewById<ProgressBar>(R.id.progressBarSplash)?.visibility=View.VISIBLE
        if ( user!=null ) {

            println(user.uid.toString())

            dtb.child("Users").child(user.uid.toString()).get().addOnSuccessListener {
                val check = it.child("infoentered").toString();

                if(check.contains("no")) {
                    fragmentload(LoginFragment())
                    println("loading ez")
                    //dtb.child("Users").child(user.uid.toString()).child("infoentered").setValue("yes")

                }
                else
                {


                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                    activity?.finish()
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

    //    private fun checkInternet(): Boolean {
//        if (CheckInternet.isConnectedToInternet(applicationContext)) {
//            Toast.makeText(
//                applicationContext, "Something went wrong! Check your network...",
//                Toast.LENGTH_SHORT
//            ).show()
//            return false
//        }
//        return true
//    }
    private fun fragmentLoad(fragment : Fragment)
    {

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authFrameLayout, fragment)
        fragmentTransaction.commit()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SplashFragment.
         */
        //Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SplashFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun fragmentload(fragment : Fragment)
    {

        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authFrameLayout, fragment)
        fragmentTransaction.commit()

    }
}