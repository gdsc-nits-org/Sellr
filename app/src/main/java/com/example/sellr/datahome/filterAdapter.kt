package com.example.sellr.datahome

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.sellr.R
import kotlinx.coroutines.NonDisposableHandle.parent

class filterAdapter(private var dataList: ArrayList<filterData>) :
    RecyclerView.Adapter<filterAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): filterAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.filter_item,
            parent, false)
        return filterAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.category.text = currentItem.category
        }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var category : TextView = itemView.findViewById(R.id.category)

        }
        }


