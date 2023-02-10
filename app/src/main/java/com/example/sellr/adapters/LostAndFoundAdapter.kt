package com.example.sellr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.R
import com.example.sellr.data.LostAndFoundData
import com.example.sellr.databinding.LayoutLostandfoundItemBinding

class LostAndFoundAdapter(val context: Context,val objectList:ArrayList<LostAndFoundData> ):
    RecyclerView.Adapter<LostAndFoundAdapter.LostAndFoundViewHolder>() {


    inner class LostAndFoundViewHolder(val view: View):RecyclerView.ViewHolder(view){

        var binding : LayoutLostandfoundItemBinding = LayoutLostandfoundItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostAndFoundViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_lostandfound_item,parent,false)
        return LostAndFoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: LostAndFoundViewHolder, position: Int) {

        val obj = objectList[position]


        holder.binding.lostandfoundObject.text= obj.objectName
        holder.binding.lostandfoundLocation.text = obj.objectLocation
        holder.binding.lostandfoundPosterUserName.text = obj.posterUser
        if (obj.imageUrl != "NONE") {
            Glide.with(context).load(obj.imageUrl).into(holder.binding.lostandfoundObjectimage)
        }
        else{
            holder.binding.lostandfoundObjectimage.visibility = View.GONE
        }

        if(obj.lostOrFound == "FOUND"){
            holder.binding.indicatorRed.visibility = View.GONE
            println("THE OBJECT WAS : ${obj.lostOrFound}" )
            println("user poster was : ${obj.posterUser}" )
        }
        else if (obj.lostOrFound == "LOST") {
            holder.binding.indicatorGreen.visibility = View.GONE
            println("THE OBJECT WAS : ${obj.lostOrFound}" )
        }
    }
    override fun getItemCount(): Int {
        return objectList.size
    }
}