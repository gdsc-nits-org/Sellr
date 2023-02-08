package com.example.sellr.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.DescriptionPage
import com.example.sellr.data.LostAndFoundData
import com.example.sellr.data.SellData
import com.example.sellr.databinding.GridLayoutBinding
import com.example.sellr.databinding.LostFoundItemCardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase





class userItemLostFoundAdapter(
    private val context: Context?,
    private var itemList: ArrayList<LostAndFoundData>
) :
    RecyclerView.Adapter<userItemLostFoundAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(val adapterBinding:LostFoundItemCardBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding = LostFoundItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)


    }



    override fun onBindViewHolder(holder: userItemLostFoundAdapter.ItemsViewHolder, position: Int) {
        //holder.adapterBinding.itemImage.setImageResource(itemList[position].imagePrimary)
        val url = itemList[position].imageUrl
        context?.let { Glide.with(it).load(url).into(holder.adapterBinding.itemImage) }

        holder.adapterBinding.itemName.text = itemList[position].objectName
        holder.adapterBinding.lostndFoundStatus.text = itemList[position].lostOrFound


//        holder.adapterBinding.soldButton.setOnClickListener {
//            //println("position to delete ${itemList[position].toString()}")
//            val builder = AlertDialog.Builder(context!!)
//            builder.setTitle("Are you sure?")
//            builder.setMessage("Your item will be marked as Sold in the database")
//            builder.setPositiveButton("Yes") { _, _ ->
//                soldModel(itemList[position].pid.toString())
//                itemList.removeAt(position)
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, itemList.size)
//
//            }
//            builder.setNegativeButton("No") { _, _ ->
//            }
//            builder.show()
//
//
//        }

        holder.adapterBinding.deleteButton.setOnClickListener {
            println("position to delete ${itemList[position]}")
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Are you sure?")
            builder.setMessage("Your item will be deleted permanently from the database")
            builder.setPositiveButton("Yes") { _, _ ->
                deleteModel(itemList[position].uid.toString())
                itemList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemList.size)

            }
            builder.setNegativeButton("No") { _, _ ->
            }
            builder.show()

        }

        //commented out because item description screen of lost and found data not yet created

//        holder.itemView.setOnClickListener {
//            val value = itemList[position].uid.toString()
//            val i = Intent(context, DescriptionPage::class.java)
//            i.putExtra("key", value)
//            context?.startActivity(i)
//        }

    }

    //function to mark item as sold not required because we cant sell what we don't have.... Can we :) ??

//    private fun soldModel(model: String) {
//
//        val database =
//            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
//                .getReference("Items")
//        database.child(model).child("sold").setValue(true).addOnSuccessListener {
//            Toast.makeText(context, "Item Marked as Sold", Toast.LENGTH_LONG).show()
//        }.addOnFailureListener {
//            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
//        }
//    }

    private fun deleteModel(model: String) {


        val databaseProd =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("LostAndFound")
        databaseProd.child(model).removeValue().addOnSuccessListener {

            val ref = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LostAndFound")
            val query: Query = ref.child("uid").orderByValue().equalTo(model)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
            Toast.makeText(context, "Item deleted from database", Toast.LENGTH_LONG).show()

        }.addOnFailureListener {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}


