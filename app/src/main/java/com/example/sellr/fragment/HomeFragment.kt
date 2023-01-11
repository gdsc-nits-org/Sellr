package com.example.sellr.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellr.R
import com.example.sellr.databinding.FragmentHomeBinding
import com.example.sellr.datahome.filterAdapter
import com.example.sellr.datahome.filterData
import com.example.sellr.datahome.items_home
import com.example.sellr.datahome.myAdapterhome
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //for items
    private lateinit var recylerView: RecyclerView
    private lateinit var datalist: ArrayList<items_home>
    private lateinit var searchList: ArrayList<items_home>
    private lateinit var searchView: SearchView
    private lateinit var dbref: DatabaseReference

    //for filer
    private lateinit var datalistforfilter : kotlin.collections.ArrayList<filterData>
    private lateinit var recylerViewfilter: RecyclerView
    private lateinit var dbreffiler: DatabaseReference



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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //for filter
        val layoutManagerfilter = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        recylerViewfilter = view.findViewById(R.id.filter)
        recylerViewfilter.layoutManager = layoutManagerfilter

        datalistforfilter = arrayListOf()

        datalistforfilter.add(filterData("All"))
        datalistforfilter.add(filterData("Electronics"))
        datalistforfilter.add(filterData("Books"))
        datalistforfilter.add(filterData("Vehicles"))
        datalistforfilter.add(filterData("Clothes"))
        datalistforfilter.add(filterData("Others"))

        recylerViewfilter.adapter = filterAdapter(datalistforfilter)



        // for items
        val layoutManager = GridLayoutManager(context, 2)
        recylerView = view.findViewById(R.id.Home_rc)
        recylerView.layoutManager = layoutManager

        searchView = view.findViewById(R.id.searchView)

        datalist = arrayListOf()
        searchList = arrayListOf()
        getUserData()



        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchList.clear()
                val searchText = p0!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    datalist.forEach{
                        if(it.productName?.toLowerCase(Locale.getDefault())?.contains(searchText) == true){
                            searchList.add(it)
                        }
                    }
                    recylerView.adapter?.notifyDataSetChanged()
                }
                else{
                    searchList.clear()
                    searchList.addAll(datalist)
                    recylerView.adapter?.notifyDataSetChanged()
                }
                return false
            }


        })




    }



    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Items")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for(userSnapshot in snapshot.children){

                        val items = userSnapshot.getValue(items_home::class.java)
                        if (items != null) {
                            if(items.sold == false) {
                                datalist.add(items!!)
                            }
                        }
                    }

                    searchList.addAll(datalist)
                    recylerView.adapter = myAdapterhome(this@HomeFragment,searchList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

                val text = "Error"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(getActivity(), text, duration)
                toast.show()
            }

        })



    }

}

