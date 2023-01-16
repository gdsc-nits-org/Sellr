package com.example.sellr.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.data.SellData
import com.example.sellr.databinding.SoldRecyclerLayoutBinding

class SoldAdapter(

    var itemList:ArrayList<SellData>):

    RecyclerView.Adapter<SoldAdapter.ItemsViewHolder>() {

        inner class ItemsViewHolder(val adapterBinding: SoldRecyclerLayoutBinding) : RecyclerView.ViewHolder(adapterBinding.root){}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
            val binding= SoldRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ItemsViewHolder(binding)


        }

        override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
            //holder.adapterBinding.itemImage.setImageResource(itemList[position].imagePrimary)
            val url=itemList[position].imagePrimary
            Glide.with(holder.adapterBinding.itemImage).load(url).centerCrop().into(holder.adapterBinding.itemImage)
            //context?.let { Glide.with(it).load(url).into(holder.adapterBinding.itemImage) }

            holder.adapterBinding.itemName.text=itemList[position].productName
            holder.adapterBinding.itemPrice.text=itemList[position].price
//            var toDelete:SoldFragment= SoldFragment()

//            holder.adapterBinding.deleteButton.setOnClickListener {
//                toDelete.toDelete(itemList[position].pId)
//            }


        }

        override fun getItemCount(): Int {
            return itemList.size
        }
}