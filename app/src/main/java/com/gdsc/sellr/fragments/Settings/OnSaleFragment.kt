package com.gdsc.sellr.fragments.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.gdsc.sellr.adapters.OnSaleAdapter
import com.gdsc.sellr.dataModels.SellDataModel
import com.gdsc.sellr.databinding.FragmentOnSaleBinding
import com.gdsc.sellr.viewModels.settings.OnSaleViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OnSaleFragment : Fragment() {

    private val viewModel: OnSaleViewModel by viewModels()
    private var viewBinding: FragmentOnSaleBinding? = null
    private val binding get() = viewBinding!!

    private lateinit var itemsAdapter: OnSaleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentOnSaleBinding.inflate(inflater, container, false)
        observeOnSaleItems()
        return binding.root
    }

    private fun observeOnSaleItems() {
        viewModel.onSaleItems.observe(viewLifecycleOwner) { itemList ->
            itemList?.let {
                itemsAdapter = OnSaleAdapter(requireContext(), it)
                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
                binding.recyclerView.adapter = itemsAdapter
                binding.emptyState.visibility =
                    if (itemList.isEmpty()) View.VISIBLE else View.GONE
                binding.recyclerView.visibility =
                    if (itemList.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveDataFromDatabase()
    }

    private fun retrieveDataFromDatabase() {
        viewModel.retrieveOnSaleItemsFromDatabase()
    }
}
