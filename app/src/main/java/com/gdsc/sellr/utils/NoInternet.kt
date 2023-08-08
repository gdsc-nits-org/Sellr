package com.gdsc.sellr.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.gdsc.sellr.MainActivity
import com.gdsc.sellr.databinding.FragmentNoInternetBinding

class noInternet : Fragment() {


    private var viewBinding: FragmentNoInternetBinding?=null
    private var lastState: Bundle? = null
    private val binding get()= viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("No Internet Bruh!")
            } })




        viewBinding=FragmentNoInternetBinding.inflate(inflater,container,false)
        val view=binding.root

        binding.retrybutton.setOnClickListener {
            if (isNetworkConnected(requireContext())){

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)


            }
            else{
                Toast.makeText(requireContext(), "Please check your internet and retry", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lastState = outState
    }



    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}