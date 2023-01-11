package com.example.sellr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.sellr.databinding.ActivityMainBinding
import com.example.sellr.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    //private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager=supportFragmentManager
        val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
        val fragement=ProfileFragment()
        fragmentTransaction.add(R.id.frame,fragement)
        fragmentTransaction.commit()


    }
}