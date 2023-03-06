package com.example.sellr.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sellr.adapters.CartRVAdapter
import com.example.sellr.data.SellData
import com.example.sellr.databinding.FragmentCartBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {

    lateinit var binding : FragmentCartBinding
    private val cartModelArrayList=ArrayList<SellData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        fetchDataFromDataBase()
        return binding.root
    }
//    private fun retriveDataFromDatabase(){
//        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
//        val myReference: DatabaseReference =database.reference.child("Items")
//        myReference.get().addOnSuccessListener {
//            val user=Firebase.auth.currentUser?.uid.toString()
//            itemList.clear()   //For clearing when data gets added to database.
//            for(eachItem in it.children){
//                val item=eachItem.getValue(SellData::class.java)
//                if(item!=null && item.userUID==user&&!item.sold!!){
//                    itemList.add(item)

    private fun fetchDataFromDataBase() {
        binding.idRVCourse.adapter= context?.let {it1-> CartRVAdapter(it1,cartModelArrayList) }
        binding.idRVCourse.layoutManager= GridLayoutManager(context,2)
        println("Fetching data for cart screen")
        val user = Firebase.auth.currentUser
        val database= FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference =database.reference.child("Users").child(
            user?.uid.toString()).child("favpost")
        myReference.get().addOnSuccessListener{

            cartModelArrayList.clear()
            //println(it.toString())
            for(cartItemIDs in it.children){
//                val item=cartItem.getValue(SellData::class.java)
//                if(item!=null )
//                {
//                    if(!item.sold!!)
//                    {
//                        cartModelArrayList.add(item)
//                    }
//
//                }
                //get item ids to fetch
                fetchIndividualItems(cartItemIDs.value.toString())
                println(cartItemIDs.value.toString())
            }

        }.addOnFailureListener {

        }

//        database.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                cartModelArrayList.clear()
//                if(snapshot.exists()){
//                    for(userSnapshot in snapshot.children){
//
//                        val items = userSnapshot.value.toString()
//
//                        val databaseItem = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Items")
//                        databaseItem.child(items).get().addOnSuccessListener {
//
//                            if (it.exists()){
//                                val sold=it.child("sold").value.toString()
//                                if(sold=="false")
//                                {
//                                    val price = it.child("price").value
//                                    val name=it.child("productName").value
//                                    val image=it.child("imagePrimary").value
//                                    val key=userSnapshot.key.toString()
//                                    val id=it.child("pid").value.toString()
//                                    cartModelArrayList.add(CartModel(name.toString(),price.toString(),image.toString(),key,id))
//
//                                }
//
//                            }
////                            if(cartModelArrayList.size==0)
////                            {
////                                emptyIV.visibility=View.VISIBLE
////                            }
////                            else
////                            {
////                                emptyIV.visibility=View.GONE
////                            }
//                        }.addOnFailureListener{
//                            TODO("Not yet implemented")
//                        }
//
//                    }
//                }
//                else
//                {
//                    binding.emptyIV.visibility=View.VISIBLE
//                }
//                cartRVAdapter.notifyDataSetChanged()
//
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }

    private fun fetchIndividualItems(itemID:String) {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference =database.reference.child("Items").child(itemID)
        myReference.get().addOnSuccessListener {

            val item=it.getValue(SellData::class.java)
            if (item != null) {
                if(item.sold != true){
                    cartModelArrayList.add(item)
                }
            }
            binding.idRVCourse.adapter?.notifyDataSetChanged()
            binding.emptyIV.visibility = if (cartModelArrayList.isEmpty()) View.VISIBLE else View.GONE
            binding.idRVCourse.visibility = if (cartModelArrayList.isEmpty()) View.GONE else View.VISIBLE

        }.addOnFailureListener {
            Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT).show()

        }
    }

}