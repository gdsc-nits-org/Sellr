package com.gdsc.sellr.adapters

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.sellr.DescriptionPage
import com.gdsc.sellr.dataModels.SellDataModel
import com.gdsc.sellr.databinding.GridViewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class OnSaleAdapter(
    private val context: Context?,
    private var itemList: ArrayList<SellDataModel>
) :
    RecyclerView.Adapter<OnSaleAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(val adapterBinding: GridViewBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding = GridViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        //holder.adapterBinding.itemImage.setImageResource(itemList[position].imagePrimary)
        val url = itemList[position].imagePrimary
        context?.let { Glide.with(it).load(url).into(holder.adapterBinding.itemImage) }

        holder.adapterBinding.itemName.text = itemList[position].productName
        holder.adapterBinding.itemPrice.text = itemList[position].price


        holder.adapterBinding.soldButton.setOnClickListener {
            //println("position to delete ${itemList[position].toString()}")
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle("Are you sure?")
            builder.setMessage("Your item will be marked as Sold in the database")
            builder.setPositiveButton("Yes") { _, _ ->
                soldModel(itemList[position].pid.toString())
                itemList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemList.size)

            }
            builder.setNegativeButton("No") { _, _ ->
            }
            builder.show()


        }

        holder.adapterBinding.deleteButton.setOnClickListener {

            if (context?.let { it1 -> isNetworkConnected(it1) } == true) {
                println("position to delete ${itemList[position]}")
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("Are you sure?")
                builder.setMessage("Your item will be deleted permanently from the database")
                builder.setPositiveButton("Yes") { _, _ ->
                    deleteModel(itemList[position].pid.toString())
                    itemList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemList.size)

                }
                builder.setNegativeButton("No") { _, _ ->
                }
                builder.show()
            }

            else{
                Toast.makeText(context, "Please Check Your Internet and Try again.", Toast.LENGTH_LONG).show()
            }

        }
        holder.itemView.setOnClickListener {
            val value = itemList[position].pid.toString()
            val i = Intent(context, DescriptionPage::class.java)
            i.putExtra("key", value)
            context?.startActivity(i)
        }

    }


    private fun soldModel(model: String) {

        val database =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Items")
        database.child(model).child("sold").setValue(true).addOnSuccessListener {
            Toast.makeText(context, "Item Marked as Sold", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
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

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}


