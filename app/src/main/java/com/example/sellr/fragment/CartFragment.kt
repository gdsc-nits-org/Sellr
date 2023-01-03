package com.example.sellr.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.R
import com.example.sellr.adapters.CartRVAdapter
import com.example.sellr.data.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_cart, container, false)
        val courseRV = view.findViewById<RecyclerView>(R.id.idRVCourse)

        // Here, we have created new array list and added data to it
        val courseModelArrayList: ArrayList<CartModel> = ArrayList()
        courseModelArrayList.add(CartModel("Watch", "2000", "https://firebasestorage.googleapis.com/v0/b/sellr-7a02b.appspot.com/o/file%2Fdeep21_ug230103085338138?alt=media&token=1d6041a1-02aa-4169-9dd1-9a2e7f7548e4"))
        courseModelArrayList.add(CartModel("Cycle", "5000", "https://firebasestorage.googleapis.com/v0/b/sellr-7a02b.appspot.com/o/file%2Fdeep21_ug2212261050481248?alt=media&token=0aa35de4-ff46-4ac7-a09f-c65f82b35337"))
        courseModelArrayList.add(CartModel("Oppo Phone", "19000", "https://firebasestorage.googleapis.com/v0/b/sellr-7a02b.appspot.com/o/file%2Fdeep21_ug230103085413113?alt=media&token=62f908bd-6227-4324-b5e7-b4dc20f7171b"))
        courseModelArrayList.add(CartModel("OnePlus phone", "23000", "https://firebasestorage.googleapis.com/v0/b/sellr-7a02b.appspot.com/o/file%2Fdeep21_ug230103085425125?alt=media&token=d82e222b-f2c2-49e9-82ab-e7187ba62bf5"))
        courseModelArrayList.add(CartModel("Head Phone", "30000", "https://firebasestorage.googleapis.com/v0/b/sellr-7a02b.appspot.com/o/file%2Fdeep21_ug2212261050481248?alt=media&token=0aa35de4-ff46-4ac7-a09f-c65f82b35337"))
        //courseModelArrayList.removeAt(1)
        //TODO: TESTING
        //testing
        val database = Firebase.database
        val myRef = database.getReference("test")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                Log.d(value,"this")
                Toast.makeText(context,value,Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val cartRVAdapter = CartRVAdapter(context, courseModelArrayList)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        courseRV.layoutManager = linearLayoutManager
        courseRV.adapter = cartRVAdapter
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}