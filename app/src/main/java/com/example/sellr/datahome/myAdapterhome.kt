package com.example.sellr.datahome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class myAdapterhome(val fragment: Fragment, private var dataList: ArrayList<items_home>): RecyclerView.Adapter<myAdapterhome.MyViewHolder>() {
    private lateinit var dtb: DatabaseReference
    var onItemClick: ((items_home) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items_home,
            parent, false)
        return MyViewHolder(itemView)
    }
    private val user= Firebase.auth.currentUser?.uid.toString()
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        dtb = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        val currentItem = dataList[dataList.size - position - 1]
        holder.newOrOld.text = currentItem.condition
        Glide.with(fragment).load(currentItem.imagePrimary).into(holder.photo)
        holder.itemName.text = currentItem.productName
        holder.price.text = currentItem.price
        holder.symbol
        var key:String=""


        dtb.child("Users").child(user).child("favpost").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var x =0;
                if(snapshot.exists()){

                    for(userSnapshot in snapshot.children){

                        val items = userSnapshot.value
                        if(items == currentItem.pid) {
                            holder.addToFav.setImageResource(R.drawable.add_to_carty)
                            currentItem.addedtofav = true
                            currentItem.key = userSnapshot.key
                            println("Item is " + holder.itemName.text + "value is " + currentItem.addedtofav + "under for loop")
                            x = 1;
                            break
                        }


                    }
                    if(x==0)
                    {

                        holder.addToFav.setImageResource(R.drawable.add_to_cart)
                        currentItem.addedtofav = false
                        println("Item is "+ holder.itemName.text+ "value is "+currentItem.addedtofav)
                    }



                }





            }

            override fun onCancelled(error: DatabaseError) {

                val text = "Error"
                val duration = Toast.LENGTH_SHORT


            }

        })

        holder.addToFav.setOnClickListener {

            if(currentItem.addedtofav==false)
            {
                println("inside pushing")
                // currentItem.addedtofav = true
                // holder.addToFav.setImageResource(R.drawable.favorite)
                dtb.child("Users").child(user).child("favpost").push().setValue(currentItem.pid)

            }
            else
            {
                holder.addToFav.setImageResource(R.drawable.add_to_cart)
                println("inside removing")
                currentItem.addedtofav = false
                holder.addToFav.setImageResource(R.drawable.add_to_cart)
                dtb.child("Users").child(user).child("favpost").child(currentItem.key.toString()).removeValue()


            }                }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val itemName : TextView = itemView.findViewById(R.id.itemname)
        val photo : ImageView = itemView.findViewById(R.id.item_image)
        val symbol : TextView = itemView.findViewById(R.id.symbol)
        val price : TextView = itemView.findViewById(R.id.price)
        val addToFav: ImageView = itemView.findViewById(R.id.addedtofav)
        val newOrOld: TextView = itemView.findViewById(R.id.used)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataList[adapterPosition])
            }
        }
    }
}