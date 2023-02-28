package com.example.sellr

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.sellr.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var hideIcon = true
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_screen, menu)
        menu?.findItem(R.id.edit_profile)?.isVisible = !hideIcon
       // menu?.findItem(R.id.logOutMenu)?.isVisible = !hideIcon
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aboutUsMenu -> {
                val i = Intent(applicationContext, MainFragmentHolder::class.java)
                i.putExtra("aboutUs", "aboutUs")
                startActivity(i)
            }
            R.id.reportUsMenu -> {
                val i = Intent(applicationContext, MainFragmentHolder::class.java)
                i.putExtra("reportUs", "reportUs")
                startActivity(i)
            }
            R.id.edit_profile -> {
                val i = Intent(applicationContext, MainFragmentHolder::class.java)
                i.putExtra("editProfile", "editProfile")
                startActivity(i)

            }

//            R.id.logOutMenu -> {
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Are you sure?")
//                builder.setMessage("You will be logged out of your account")
//                builder.setPositiveButton("Yes") { _, _ ->
//                    Firebase.auth.signOut()
//                    val intent = Intent(applicationContext, AuthActivity::class.java)
//                    intent.putExtra("splash off", "splash off")
//                    startActivity(intent)
//                    finish()
//
//                }
//                builder.setNegativeButton("No") { _, _ ->
//                }
//                builder.show()
//
//
//            }
            else -> {
                Toast.makeText(applicationContext, "none", Toast.LENGTH_LONG).show()
            }
        }

        return (super.onOptionsItemSelected(item))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        supportActionBar?.setDisplayUseLogoEnabled(true)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav)
        binding.bottomBar.setupWithNavController(popupMenu.menu, navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.cartFragment -> "My Cart"
                R.id.profile -> "Profile"

                R.id.lostAndFound -> "Lost/Found"
                else -> "Sellr"
            }
            when (destination.id) {
                R.id.profile -> {
                    hideIcon = false
                    invalidateOptionsMenu()
                }
                else -> {
                    hideIcon = true
                    invalidateOptionsMenu()
                }
            }
        }

    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}