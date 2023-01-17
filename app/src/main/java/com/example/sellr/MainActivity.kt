package com.example.sellr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.sellr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_screen, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.aboutUsMenu -> {
                val i = Intent(applicationContext,MainFragmentHolder::class.java)
                i.putExtra("aboutUs", "aboutUs")
                startActivity(i)
            }
            R.id.reportUsMenu -> {
                val i = Intent(applicationContext,MainFragmentHolder::class.java)
                i.putExtra("reportUs", "reportUs")
                startActivity(i)
            }
            else -> {
                Toast.makeText(applicationContext,"none",Toast.LENGTH_LONG).show()
            }
        }

    return(super.onOptionsItemSelected(item));

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav)
        binding.bottomBar.setupWithNavController(popupMenu.menu, navController)

        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                title = when(destination.id){
                    R.id.cartFragment -> "My Cart"
                    R.id.profileFragment -> "Profile"
                    else -> "Sellr"
                }
            }

        })

    }
}