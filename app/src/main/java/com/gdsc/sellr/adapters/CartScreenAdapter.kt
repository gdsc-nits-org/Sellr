package com.gdsc.sellr.adapters

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.sellr.DescriptionPage
import com.gdsc.sellr.dataModels.SellDataModel
import com.gdsc.sellr.databinding.CartLayoutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*

private fun checkForInternet(context: Context?): Boolean {

    // register activity with the connectivity manager service
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // if the android version is equal to M
    // or greater we need to use the
    // NetworkCapabilities to check what type of
    // network has the internet connection
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            // else return false
            else -> false
        }
    } else {
        // if the android version is below M
        @Suppress("DEPRECATION") val networkInfo =
            connectivityManager.activeNetworkInfo ?: return false
        @Suppress("DEPRECATION")
        return networkInfo.isConnected
    }
}

class CartScreenAdapter(private val context: Context?,
                        private var cartModelArrayList: ArrayList<SellDataModel>) :
    RecyclerView.Adapter<CartScreenAdapter.ItemsViewHolder>() {

   // private val cartModelArrayList: ArrayList<SellData>
   inner class ItemsViewHolder(val adapterBinding: CartLayoutBinding) :
       RecyclerView.ViewHolder(adapterBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        // to inflate the layout for each item of recycler view.
       val binding=CartLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val model: SellDataModel = cartModelArrayList[position]

        var textRqrd = model.productName
        if (textRqrd!!.length >= 11) {
            val dots = "..."
            textRqrd = textRqrd.substring(0, 11) + dots
        }

        holder.adapterBinding.itemName.text=textRqrd
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

        holder.adapterBinding.itemPrice.text = "Rs. ${model.price}"
        Glide.with(holder.adapterBinding.itemImage).load(model.imagePrimary).centerCrop().into(holder.adapterBinding.itemImage)
        val btn = holder.adapterBinding.removeButton
        btn.setOnClickListener {
            if (checkForInternet(context)) {
                println(model.pid.toString())
                deleteModel(model.pid.toString())
                //cartModelArrayList.removeAt(position)
                //notifyItemRemoved(position)
                //notifyItemRangeChanged(position, cartModelArrayList.size)

            } else {
                Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show()
            }
        }
        holder.itemView.setOnClickListener {
            val value = cartModelArrayList[position].pid.toString()
            val i = Intent(context, DescriptionPage::class.java)
            i.putExtra("key", value)
            context?.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return cartModelArrayList.size
    }

    private fun deleteModel(itemID: String) {
        val user = Firebase.auth.currentUser?.uid.toString()
        val dtb =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        val query: Query =
            dtb.child("Users").child(user).child("favpost").orderByValue()
                .equalTo(itemID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                }

                Toast.makeText(
                    context,
                    "Item Removed from Cart",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

}
