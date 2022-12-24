package com.example.sellr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sellr.databinding.ActivityAuthBinding
import com.example.sellr.fragment.RegisterFragment

lateinit var binding : ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentload(LoginFragment())




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
        super.onBackPressed()
        val currentFragment = supportFragmentManager.fragments.last()
        if(currentFragment.toString().contains("RegisterFragment")) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
        else {
            finish()
        }


    }


}