package com.example.sellr

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sellr.databinding.FragmentNoInternetBinding
import com.example.sellr.databinding.FragmentProfileBinding

class noInternet : Fragment() {


    private var viewBinding: FragmentNoInternetBinding?=null
    private val binding get()= viewBinding!!

    private var previousFragmentName: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentNoInternetBinding.inflate(inflater,container,false)
        val view=binding.root

        arguments?.let {
            previousFragmentName = it.getString("previousFragmentName")
        }
        binding.retrybutton.setOnClickListener {
            if (isNetworkConnected(requireContext())){
                previousFragmentName?.let {
                    parentFragmentManager.popBackStack(it, 0)
                }

            }
            else{
                Toast.makeText(requireContext(), "Please check your internet and retry", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}