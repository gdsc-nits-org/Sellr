package com.example.sellr.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sellr.R
import com.example.sellr.data.CartModel

class CartRVAdapter(private val context: Context?, courseModelArrayList: ArrayList<CartModel>) :
    RecyclerView.Adapter<CartRVAdapter.ViewHolder>() {
    private val courseModelArrayList: ArrayList<CartModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cart_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: CartModel = courseModelArrayList[position]
        holder.courseNameTV.text=model.item_name
        holder.courseRatingTV.text=model.item_price
        Glide.with(holder.courseIV).load(model.item_image).centerCrop().into(holder.courseIV)
        val btn=holder.removeButton
        btn.setOnClickListener {
            courseModelArrayList.removeAt(position)
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, courseModelArrayList.size);
            Toast.makeText(context, "Item Removed $position",Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseIV: ImageView
        val courseNameTV: TextView
        val courseRatingTV: TextView
        val removeButton : Button
        init {
            courseIV = itemView.findViewById(R.id.item_image)
            courseNameTV = itemView.findViewById(R.id.item_name)
            courseRatingTV = itemView.findViewById(R.id.item_price)
            removeButton=itemView.findViewById(R.id.remove_button)

        }

    }

    // Constructor
    init {
        this.courseModelArrayList = courseModelArrayList
    }
}
