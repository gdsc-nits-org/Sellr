// SoldFragment.kt
package com.gdsc.sellr.fragments.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdsc.sellr.adapters.SoldItemsAdapter
import com.gdsc.sellr.databinding.FragmentSoldBinding
import com.gdsc.sellr.viewModels.settings.SoldViewModel

class SoldFragment : Fragment() {

    private val viewModel: SoldViewModel by viewModels()
    private var viewBinding: FragmentSoldBinding? = null
    private val binding get() = viewBinding!!
    private lateinit var itemsAdapter: SoldItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSoldBinding.inflate(inflater, container, false)
        observeSoldItems()
        return binding.root
    }

    private fun observeSoldItems() {
        viewModel.soldItems.observe(viewLifecycleOwner) { soldItemList ->
            soldItemList.let {
                itemsAdapter = SoldItemsAdapter(requireContext(), it)
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                binding.recyclerView.adapter = itemsAdapter

                // Add other logic for empty state, loading indicator, etc.

                binding.emptyState.visibility = if (soldItemList.isEmpty()) View.VISIBLE else View.GONE
                binding.recyclerView.visibility = if (soldItemList.isEmpty()) View.GONE else View.VISIBLE

            }
        }

        viewModel.retrieveSoldItemsFromDatabase()
    }
}
