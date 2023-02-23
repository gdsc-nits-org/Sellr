package com.example.sellr

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sellr.fragment.*

class MainFragmentHolder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment_holder)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        val intent: Intent = intent
        if(intent.hasExtra("sold"))
        {
            title="Sold items"
            fragmentLoad(SoldFragment())
        }
        else if(intent.hasExtra("onSale"))
        {
            title="Item on sale"
            fragmentLoad(OnSaleFragment())
        }
        else if(intent.hasExtra("aboutUs"))
        {
                title="About Us"
            fragmentLoad(AboutFragment())
        }

        else if(intent.hasExtra("developers"))
        {
            title="Developers"
            fragmentLoad(AboutUsFragment())
        }
        else if(intent.hasExtra("editProfile"))
        {
            title="Edit Profile"
            fragmentLoad(fragmentEditProfile())
        }
        else if(intent.hasExtra("reportUs"))
        {
            title="Report an issue"
            fragmentLoad(ReportUsFragment())
        }
        else if(intent.hasExtra("lostAndFoundList"))
        {
            title="User Lost and Found"
            fragmentLoad(UserItemLostAndFound())
        }

        else if(intent.hasExtra("extraDetails"))
        {
            try {
                this.supportActionBar!!.hide()
            } // catch block to handle NullPointerException
            catch (_: NullPointerException) {
            }
            fragmentLoad(fragment_extradetails())
        }
    }
    private fun fragmentLoad(fragment : Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction= fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainAlterFrameLayout,fragment)
        fragmentTransaction.commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}