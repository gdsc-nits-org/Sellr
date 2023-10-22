package com.gdsc.sellr.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import android.widget.PopupMenu
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.gdsc.sellr.R
import com.gdsc.sellr.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val bottomNavigationView = binding.bottomNavigation

        setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                else -> {
                    supportActionBar?.show()
                    binding.bottomNavigation.visibility = android.view.View.VISIBLE
                }
//                in listOf(
////                    R.id.splashFragment,
////                    R.id.myAccountFragment,
////                    R.id.settingsFragment,
////                    R.id.notificationsSettingsFragment,
////                    R.id.notificationsFragment,
////                    R.id.deactivateAccountFragment
//                ) -> {
//                    binding.bottomNavigation.visibility = android.view.View.GONE
//                }
//                else -> {
//                    supportActionBar?.show()
//                    binding.bottomNavigation.visibility = android.view.View.VISIBLE
//                }
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        super.onBackPressed()
    }
}