package com.gdsc.sellr.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gdsc.sellr.model.Sell
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import com.gdsc.sellr.R
import com.gdsc.sellr.data.repository.MainRepo
import com.gdsc.sellr.data.repository.MainRepoImpl
import com.gdsc.sellr.databinding.ItemsHomeBinding
import com.google.firebase.database.DatabaseReference
import com.sun.mail.imap.protocol.FetchResponse.getItem
import javax.inject.Inject

class AllSellsHomeAdapter : ListAdapter<Sell, AllSellsHomeAdapter.ItemViewHolder>(ItemComparator) {

    class ItemViewHolder(private val binding: ItemsHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Sell) {
            // Bind data to the views in your XML layout
            binding.itemname.text = item.productName
            binding.used.text = item.condition
            binding.price.text = "Rs. ${item.price}"

            // Load an image using Glide or your preferred image-loading library
            Glide.with(binding.root.context)
                .load(item.imagePrimary)
                .into(binding.itemImage)

            // Set click listeners or other actions as needed
            // For example, you can set an OnClickListener for the whole item view.
            binding.root.setOnClickListener {
                // Handle item click here
            }
        }
    }

    object ItemComparator : DiffUtil.ItemCallback<Sell>() {
        override fun areItemsTheSame(oldItem: Sell, newItem: Sell): Boolean {
            // Implement logic to check if items are the same
            return oldItem.pid == newItem.pid
        }

        override fun areContentsTheSame(oldItem: Sell, newItem: Sell): Boolean {
            // Implement logic to check if item contents are the same
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemsHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}