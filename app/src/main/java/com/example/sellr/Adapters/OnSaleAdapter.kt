package com.example.sellr.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.MainActivity
import com.example.sellr.data.SellData
import com.example.sellr.databinding.GridLayoutBinding

class OnSaleAdapter(
    var context: FragmentActivity?,
    var itemList:ArrayList<SellData>):
    RecyclerView.Adapter<OnSaleAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(val adapterBinding:GridLayoutBinding) : RecyclerView.ViewHolder(adapterBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
       val binding=GridLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemsViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        //holder.adapterBinding.itemImage.setImageResource(itemList[position].imagePrimary)
        val url=itemList[position].imagePrimary
        context?.let { Glide.with(it).load(url).into(holder.adapterBinding.itemImage) }

        holder.adapterBinding.itemName.text=itemList[position].productName
        holder.adapterBinding.itemPrice.text=itemList[position].price


    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}


