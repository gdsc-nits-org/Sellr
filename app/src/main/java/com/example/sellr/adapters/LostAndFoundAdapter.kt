package com.example.sellr.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.R
import com.example.sellr.data.LostAndFoundData

class LostAndFoundAdapter(val context: Context,val objectList:ArrayList<LostAndFoundData> ):
    RecyclerView.Adapter<LostAndFoundAdapter.LostAndFoundViewHolder>() {

    inner class LostAndFoundViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val objName = view.findViewById<TextView>(R.id.lostandfoundObject)
        val objLocation = view.findViewById<TextView>(R.id.lostandfoundLocation)
        val usrContact = view.findViewById<TextView>(R.id.lostandfoundUserContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostAndFoundViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_lostandfound_item,parent,false)
        return LostAndFoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: LostAndFoundViewHolder, position: Int) {

        val newList = objectList[position]
        holder.objName.text = newList.objectName
        holder.objLocation.text = newList.objectLocation
        holder.usrContact.text = newList.contactNumber



    }
    override fun getItemCount(): Int {
        return objectList.size
    }


}