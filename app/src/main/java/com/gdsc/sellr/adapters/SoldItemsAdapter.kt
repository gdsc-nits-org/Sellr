package com.gdsc.sellr.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.sellr.DescriptionPage
import com.gdsc.sellr.dataModels.SellDataModel
import com.gdsc.sellr.databinding.SoldRecyclerLayoutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SoldItemsAdapter(
    private val context: Context?,
    var itemList: List<SellDataModel>
):

    RecyclerView.Adapter<SoldItemsAdapter.ItemsViewHolder>() {

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

            holder.adapterBinding.deleteButton.setOnClickListener {
                println("position to delete ${itemList[position]}")
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Are you sure?")
                builder.setMessage("Your item will be deleted permanently from the database")
                builder.setPositiveButton("Yes") { _, _ ->
                    deleteModel(itemList[position].pid.toString())
                    itemList = itemList.toMutableList().apply { removeAt(position) }
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemList.size)

                }
                builder.setNegativeButton("No") { _, _ ->
                }
                builder.show()
            }

            holder.itemView.setOnClickListener {
                val value = itemList[position].pid.toString()
                val i = Intent(context, DescriptionPage::class.java)
                i.putExtra("key", value)
                context?.startActivity(i)
            }


        }
    private fun deleteModel(model: String) {


        val databaseProd =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Items")
        databaseProd.child(model).removeValue().addOnSuccessListener {

            val ref = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
            val query: Query = ref.child(Firebase.auth.currentUser?.uid.toString()).child("pId").orderByValue().equalTo(model)

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