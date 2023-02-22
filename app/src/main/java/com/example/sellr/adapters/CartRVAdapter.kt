package com.example.sellr.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.R
import com.example.sellr.data.CartModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class CartRVAdapter(private val context: Context?, cartModelArrayList: ArrayList<CartModel>) :
    RecyclerView.Adapter<CartRVAdapter.ViewHolder>() {
    var onItemClick: ((CartModel) -> Unit)? = null
    private val cartModelArrayList: ArrayList<CartModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cart_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: CartModel = cartModelArrayList[position]

        var textRqrd=model.item_name
        if(textRqrd.length >=11){
            val dots="..."
            textRqrd=textRqrd.substring(0,11)+dots
        }

        holder.cartNameTV.text=textRqrd
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

//        if(itemList[position].objectName!!.length >= 15){
//            val dots="..."
//            val textRqrd=itemList[position].objectName?.substring(0,12)+dots
//            holder.adapterBinding.itemName.text=textRqrd
//
//        }
//        else{
//            holder.adapterBinding.itemName.text = itemList[position].objectName
//        }
//

        holder.cartPriceTV.text="Rs. ${model.item_price}"
        Glide.with(holder.cartIV).load(model.item_image).centerCrop().into(holder.cartIV)
        val btn=holder.removeButton
        btn.setOnClickListener {
            cartModelArrayList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartModelArrayList.size)
            deleteModel(model.key)
        }
    }
    override fun getItemCount(): Int {
        return cartModelArrayList.size
    }
    private fun deleteModel(model: String){

        val database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
        database.child(Firebase.auth.currentUser?.uid.toString()).child("favpost").child(model).removeValue().addOnSuccessListener {
            Toast.makeText(context,"Item Removed From Cart",Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()
        }
    }
    // View holder class for initializing of your views such as TextView and Imageview.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartIV: ImageView
        val cartNameTV: TextView
        val cartPriceTV: TextView
        val removeButton : ImageButton
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(cartModelArrayList[adapterPosition])
            }
            cartIV = itemView.findViewById(R.id.item_image)
            cartNameTV = itemView.findViewById(R.id.item_name)
            cartPriceTV = itemView.findViewById(R.id.item_price)
            removeButton=itemView.findViewById(R.id.remove_button)

        }

    }

    // Constructor
    init {
        this.cartModelArrayList = cartModelArrayList
    }
}
