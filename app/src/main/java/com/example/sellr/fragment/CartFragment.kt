package com.example.sellr.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.DescriptionPage
import com.example.sellr.R
import com.example.sellr.adapters.CartRVAdapter
import com.example.sellr.data.CartModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_cart, container, false)
        val cart = view.findViewById<RecyclerView>(R.id.idRVCourse)
        val emptyIV=view.findViewById<ConstraintLayout>(R.id.emptyIV)
        val cartModelArrayList: ArrayList<CartModel> = ArrayList()
        val cartRVAdapter = CartRVAdapter(context, cartModelArrayList)
        //name is linear but view is in grid
        val linearLayoutManager = GridLayoutManager(context, 2)
        cart.layoutManager = linearLayoutManager
        cart.adapter = cartRVAdapter

        cartRVAdapter.onItemClick = { product ->

            val value = product.itemID
            val i = Intent(activity, DescriptionPage::class.java)
            i.putExtra("key", value)
            startActivity(i)
        }



        val user = Firebase.auth.currentUser
        val database= FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child(
            user?.uid.toString()).child("favpost")
        var count=0
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                count++
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){

                        val items = userSnapshot.value.toString()

                        val databaseItem = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")
                        databaseItem.child(items).get().addOnSuccessListener {

                            if (it.exists()){
                                val sold=it.child("sold").value.toString()
                                if(sold=="false" && count==1)
                                {
                                    val price = it.child("price").value
                                    val name=it.child("productName").value
                                    val image=it.child("imagePrimary").value
                                    val key=userSnapshot.key.toString()
                                    val id=it.child("pid").value.toString()
                                    cartModelArrayList.add(CartModel(name.toString(),price.toString(),image.toString(),key,id))
                                    cartRVAdapter.notifyItemInserted(cartModelArrayList.size-1)

                                }

                            }
                            if(cartModelArrayList.size==0)
                            {
                                emptyIV.visibility=View.VISIBLE
                            }
                            else
                            {
                                emptyIV.visibility=View.GONE
                            }
                        }.addOnFailureListener{
                            TODO("Not yet implemented")
                        }

                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        
        return view
    }
    
}