package com.example.sellr.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.LostAndFoundDescriptionPage
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
        //holder.binding.lostandfoundUserContact.text = obj.contactNumber

        holder.binding.lostandfoundPosterUserName.text =  obj.posterUser
       // holder.binding.lostandfoundDescription.text = obj.objectDescription
        if (obj.imageUrl != "NONE") {
            Glide.with(context).load(obj.imageUrl).into(holder.binding.lostandfoundObjectimage)
        }
        else{
            holder.binding.lostandfoundObjectimage.visibility = View.GONE
        }

        if(obj.lostOrFound == "FOUND"){
            holder.binding.indicatorRed.visibility = View.GONE
            println("THE OBJECT WAS : ${obj.lostOrFound}" )
        }
        else if (obj.lostOrFound == "LOST") {
            holder.binding.indicatorGreen.visibility = View.GONE
            println("THE OBJECT WAS : ${obj.lostOrFound}" )
        }

        holder.itemView.setOnClickListener {
            val value = objectList[position].pid.toString()
            val i = Intent(context, LostAndFoundDescriptionPage::class.java)
            i.putExtra("key", value)
            context?.startActivity(i)
        }


    }
    override fun getItemCount(): Int {
        return objectList.size
    }
}