package com.gdsc.sellr.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gdsc.sellr.R
import com.gdsc.sellr.adapter.AllSellsHomeAdapter
import com.gdsc.sellr.databinding.FragmentHomeBinding
import com.gdsc.sellr.ui.fragment.home.viewmodel.HomeViewModel
import com.gdsc.sellr.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth  : FirebaseAuth
    private val viewModel : HomeViewModel by viewModels()
    private val adapter by lazy { AllSellsHomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
//        (activity as AppCompatActivity).supportActionBar?.hide()
//        (activity as AppCompatActivity).setSupportActionBar(binding.homeToolbar)

        binding.HomeRc.adapter = adapter
        fetchAllSells()
        observeViewModel()

//        auth = FirebaseAuth.getInstance()
        binding.apply {

        }

        return view
    }

    private fun observeViewModel() {
        viewModel.allSells.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    isLoading(true)
//                    binding.progressBar.isVisible = true
                }
                is Resource.Error -> {
                    isLoading(false)
                    Toast.makeText(requireContext(), it.string, Toast.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    isLoading(false)
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun fetchAllSells(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getAllSells()
        }
    }

//    private fun hideViews(){
//        binding.textView9.isVisible = false
//        binding.textView7.isVisible = false
//        binding.imageView12.isVisible = false
//        binding.textView12.isVisible = false
//        binding.textView13.isVisible = false
//        binding.imageView13.isVisible = false
//        binding.tv14.isVisible = false
//        binding.textView6.isVisible = false
//        binding.imageView15.isVisible = false
//    }
//    private fun showViews(){
//        binding.textView9.isVisible = true
//        binding.textView7.isVisible = true
//        binding.imageView12.isVisible = true
//        binding.textView12.isVisible = true
//        binding.textView13.isVisible = true
//        binding.imageView13.isVisible = true
//        binding.tv14.isVisible = true
//        binding.textView6.isVisible = true
//        binding.imageView15.isVisible = true
//    }

    private fun isLoading(isLoading: Boolean){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.isLoading(isLoading)
        }
        if(isLoading){
            viewModel.isLoading.observe(viewLifecycleOwner){
                if(it){
//                    hideViews()
//                    binding.loadingAnimationHome.isVisible = false
//                    binding.progressBar.isVisible = true
                }
                else{
//                    binding.loadingAnimationHome.isVisible = false;
//                    showViews()
//                    binding.progressBar.isVisible = false
                }
            }
        }
    }

}