package com.example.sellr.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.sellr.R
import com.example.sellr.databinding.FragmentAboutUsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class AboutUsFragment : Fragment() {

    private var viewBinding :FragmentAboutUsBinding?=null
    private val binding get()= viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=FragmentAboutUsBinding.inflate(inflater,container,false)
        binding.ayush.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100058445922286",
                "instagram.com/the.ayush.gupta/","https://www.linkedin.com/in/gupta--ayush/",
            "https://github.com/ayush-gupta-git")
        }
        binding.bhaskar.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/bhaskar.wary.100",
                "https://instagram.com/wary_bhaskar?igshid=YmMyMTA2M2Y=","https://www.linkedin.com/in/bhaskar-wary/",
                "https://github.com/ayush-gupta-git")
        }
        binding.navneet.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/navneetrajkarn/","https://www.linkedin.com/in/navneet-raj-08a720228/",
                "https://github.com/navneet098")
        }

        binding.deep.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/_deep_saikia/","https://www.linkedin.com/in/deep-saikia",
                "https://github.com/saikiaDeep")
        }

        binding.sameer.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/_interstellar07_/","https://www.linkedin.com/in/sameer-zaidi-541261226/",
                "https://github.com/Interstellar07")
        }

        binding.prateek.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/_urdidact_/","https://www.linkedin.com/in/prateek-mogha-5b44b9229/",
                "https://github.com/Shadow-of-sundered-star")
        }

        binding.arpit.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "","https://www.linkedin.com/in/arpit-saikia-b82093241",
                "https://github.com/AS-Saikia")
        }

        binding.dipan.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/dipanpatgiri_/","https://www.linkedin.com/in/dipan-patgiri-a04473148/",
                "https://github.com/Dipan-17")
        }

        binding.diptabh.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://instagram.com/diptabhm?igshid=ZDdkNTZiNTM=","https://www.linkedin.com/in/diptabh-medhi-4836a8229/",
                "https://github.com/diptabhm")
        }

        binding.bibhas.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/bibhas.naskar_/","http://www.linkedin.com/in/bibhas-naskar-6596aa22b",
                "https://github.com/Bibhas-programmed")
        }

        binding.hritika.setOnClickListener{
            bottomSheetFunction("https://www.facebook.com/profile.php?id=100080411300265",
                "https://www.instagram.com/hrxclarachan/","https://www.linkedin.com/in/hritika-roy-56696923b",
                "https://github.com/hritika1404")
        }
        return binding.root
    }

    private fun gotoUrl(url: String) {
        val uri = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun bottomSheetFunction(
        facebookUrl: String?,
        instaUrl: String?,
        linkedInUrl: String?,
        githubUrl: String?
    ) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomsheet: View = LayoutInflater.from(context).inflate(
            R.layout.fragment_bottom_sheet,
            requireView().findViewById<View>(R.id.bottom_sheet_container) as? ConstraintLayout
        )

        //hooks
        val github = bottomsheet.findViewById<ImageView>(R.id.github_icon)
        val linkedin = bottomsheet.findViewById<ImageView>(R.id.linked_icon)
        val instagram = bottomsheet.findViewById<ImageView>(R.id.instagram_icon)
        val facebook = bottomsheet.findViewById<ImageView>(R.id.facebook_icon)
        github.setOnClickListener {
            githubUrl?.let { gotoUrl(it) } ?: Toast.makeText(
                activity,
                "Not Available",
                Toast.LENGTH_SHORT
            )
        }
        linkedin.setOnClickListener {
            linkedInUrl?.let { gotoUrl(it) } ?: Toast.makeText(
                activity,
                "Not Available",
                Toast.LENGTH_SHORT
            )
        }
        instagram.setOnClickListener {
            instaUrl?.let { gotoUrl(it) } ?: Toast.makeText(
                activity,
                "Not Available",
                Toast.LENGTH_SHORT
            )
        }
        facebook.setOnClickListener {
            facebookUrl?.let { gotoUrl(it) } ?: Toast.makeText(
                activity,
                "Not Available",
                Toast.LENGTH_SHORT
            )
        }
        bottomSheetDialog.setContentView(bottomsheet)
        bottomSheetDialog.show()
    }


}