package com.gdsc.sellr.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.sellr.R
import com.gdsc.sellr.dataModels.FilterHomeDataModel
import com.google.android.material.button.MaterialButton


class HomeFilterAdapter(private var dataList: ArrayList<FilterHomeDataModel>) :
    RecyclerView.Adapter<HomeFilterAdapter.MyViewHolder>() {

    var selectedItemPosition: Int = 0
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(category: String)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.filter_item,
            parent, false)
        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.category.text = currentItem.category

        holder.itemView.findViewById<MaterialButton>(R.id.category).setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()
            mListener.onItemClick(currentItem.category)
        }

        if(selectedItemPosition == position)
            holder.itemView.findViewById<MaterialButton>(R.id.category).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F9CD08"))
        else
            holder.itemView.findViewById<MaterialButton>(R.id.category).backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        var category : Button = itemView.findViewById(R.id.category)

        }

        }


