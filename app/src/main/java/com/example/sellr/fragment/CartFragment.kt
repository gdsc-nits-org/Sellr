package com.example.sellr.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.R
import com.example.sellr.adapters.CartRVAdapter
import com.example.sellr.data.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_cart, container, false)
        val cart = view.findViewById<RecyclerView>(R.id.idRVCourse)
        val cartModelArrayList: ArrayList<CartModel> = ArrayList()
        val cartRVAdapter = CartRVAdapter(context, cartModelArrayList)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        cart.layoutManager = linearLayoutManager
        cart.adapter = cartRVAdapter
        val database= FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users").child("9BBFtFinnoUNc388iUKm7AbPKrs2").child("favpost")
        var count=0
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                count++
                if(count==1)
                {
                    if(snapshot.exists()){
                        for(userSnapshot in snapshot.children){

                            val items = userSnapshot.value.toString()

                            val databaseItem = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")
                            databaseItem.child(items).get().addOnSuccessListener {

                                if (it.exists()){
                                    val sold=it.child("sold").value.toString()
                                    if(sold=="false")
                                    {
                                        val price = it.child("price").value
                                        val name=it.child("productName").value
                                        val image=it.child("imagePrimary").value
                                        val key=userSnapshot.key.toString()
                                        cartModelArrayList.add(CartModel(name.toString(),price.toString(),image.toString(),false,key))
                                        cartRVAdapter.notifyItemInserted(cartModelArrayList.size-1)

                                    }

                                }

                            }.addOnFailureListener{
                                TODO("Not yet implemented")
                            }

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