package com.gdsc.sellr.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.sellr.LostAndFoundDescriptionPage
import com.gdsc.sellr.dataModels.LostAndFoundDataModel
import com.gdsc.sellr.databinding.LostFoundItemCardBinding
import com.google.firebase.database.*


class UserLNFAdapter(
    private val context: Context?,
    private var itemList: ArrayList<LostAndFoundDataModel>
) :
    RecyclerView.Adapter<UserLNFAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(val adapterBinding: LostFoundItemCardBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val binding =
            LostFoundItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)


    }


    override fun onBindViewHolder(holder: UserLNFAdapter.ItemsViewHolder, position: Int) {
        //holder.adapterBinding.itemImage.setImageResource(itemList[position].imagePrimary)
        val url = itemList[position].imagePrimary

        if (url=="")
        {
           // holder.adapterBinding.itemImage.setImageResource(R.drawable.no_image)
        }
        else{
        context?.let { Glide.with(it).load(url).into(holder.adapterBinding.itemImage) }}



        //holder.adapterBinding.itemName.text = itemList[position].objectName

        if(itemList[position].objectName!!.length >= 15){
            val dots="..."
            val textRqrd=itemList[position].objectName?.substring(0,12)+dots
            holder.adapterBinding.itemName.text=textRqrd

        }
        else{
            holder.adapterBinding.itemName.text = itemList[position].objectName
        }
        holder.adapterBinding.lostndFoundStatus.text = itemList[position].lostOrFound
        holder.adapterBinding.lostndFoundLocation.text = itemList[position].objectLocation


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
                deleteModel(itemList[position].pid.toString())
                itemList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemList.size)


            }
            builder.setNegativeButton("No") { _, _ ->
            }
            builder.show()

        }

        //click the recycler view to open the description page

        holder.itemView.setOnClickListener {
            val value = itemList[position].pid.toString()
            val i = Intent(context, LostAndFoundDescriptionPage::class.java)
            i.putExtra("key", value)
            context?.startActivity(i)
        }

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
////
////            val ref = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LostAndFound")
////
////            val query: Query = ref.child(Firebase.auth.currentUser?.uid.toString()).child("pid").orderByValue().equalTo(model)
//
//           // Toast.makeText(context,query.toString(),Toast.LENGTH_SHORT).show()
//
//
////            val ref = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LostAndFound")
////            val query: Query = ref.child(Firebase.auth.currentUser?.uid.toString()).child("objectName").orderByValue().equalTo(model)
//
////            query.addListenerForSingleValueEvent(object : ValueEventListener {
////                override fun onDataChange(dataSnapshot: DataSnapshot) {
////                    for (snapshot in dataSnapshot.children) {
////                        snapshot.ref.removeValue()
////                    }
////                }
////
////                override fun onCancelled(databaseError: DatabaseError) {
////                }
////            })

            Toast.makeText(context, "Item deleted from database", Toast.LENGTH_LONG).show()

        }.addOnFailureListener {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}


