package com.example.sellr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sellr.databinding.ActivityAuthBinding
import com.example.sellr.fragment.RegisterFragment

lateinit var binding : ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent:Intent= intent
        if(intent.hasExtra("splash off"))
        {
            fragmentload(LoginFragment())
        }
        else
        {
            fragmentload(SplashFragment())
        }






    }
    private fun fragmentload(fragment : Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransactionn= fragmentManager.beginTransaction()
        fragmentTransactionn.replace(R.id.authFrameLayout,fragment)
        fragmentTransactionn.commit()

    }
    override fun onBackPressed() {
        println("Hi there")
        val currentFragment = supportFragmentManager.fragments.last()
        super.onBackPressed()
        if(currentFragment.toString().contains("RegisterFragment") || currentFragment.toString().contains("fragment_forgotpass") ) {

            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra("splash off","splash off")
            startActivity(intent)
        }
        else {
            finish()
        }




    }


}