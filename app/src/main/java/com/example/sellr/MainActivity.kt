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
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
//        val navController = navHostFragment!!.findNavController()
//
//        val popupMenu = PopupMenu(this, null)
//        popupMenu.inflate(R.menu.bottom_nav)
//        binding.bottomBar.setupWithNavController(popupMenu.menu, navController)
//
//        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
//            override fun onDestinationChanged(
//                controller: NavController,
//                destination: NavDestination,
//                arguments: Bundle?
//            ) {
//                title = when(destination.id){
//                    R.id.cartFragment -> "Cart"
//                    R.id.profileFragment -> "Profile"
//                    else -> "Sellr"
//                }
//            }
//
//        })

    }
}