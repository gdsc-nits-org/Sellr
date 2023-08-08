package com.gdsc.sellr.fragment

//import android.widget.SearchView
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gdsc.sellr.SellActivity
import com.gdsc.sellr.adapters.HomeFilterAdapter
import com.gdsc.sellr.dataModels.FilterHomeDataModel
import com.gdsc.sellr.dataModels.ItemsHomeDataModel
import com.gdsc.sellr.adapters.HomeItemAdapter
import com.gdsc.sellr.R
import com.gdsc.sellr.utils.CheckInternet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import java.util.*


class HomeFragment : Fragment() {

    //for items
    private lateinit var recylerView: RecyclerView
    private lateinit var datalist: ArrayList<ItemsHomeDataModel>
    private lateinit var searchList: ArrayList<ItemsHomeDataModel>
    private lateinit var searchView: SearchView
    private lateinit var dbref: DatabaseReference

    //for filer
    private lateinit var datalistforfilter: ArrayList<FilterHomeDataModel>
    private lateinit var recylerViewfilter: RecyclerView

    private lateinit var emptyH : ConstraintLayout
    private lateinit var loadingAnimationControl : LinearLayout

    //for filtered datalist in myadapterhome
    private lateinit var datalistforfilteredmyAdapter: ArrayList<ItemsHomeDataModel>

    private lateinit var goToTopButton: ExtendedFloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<ExtendedFloatingActionButton>(R.id.sell_button).setOnClickListener {

            val i = Intent(context, SellActivity::class.java)
            startActivity(i)


        }

        return view

    }

    override fun onPause() {
        searchView.setQuery("", false); // clear the text
        searchView.clearFocus()
        searchView.isIconified = true;
        defaultFilter="All"
        super.onPause()
    }
    var defaultFilter="All"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyH = view.findViewById(R.id.emptyhome)
        loadingAnimationControl=view.findViewById(R.id.loadingAnimation_home)
        goToTopButton=view.findViewById(R.id.top_scroll_button)



        //for filter
        val layoutManagerfilter =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recylerViewfilter = view.findViewById(R.id.filter)
        recylerViewfilter.layoutManager = layoutManagerfilter

        //to scroll back to top
        goToTopButton.setOnClickListener {
            recylerView.smoothScrollToPosition(0)
        }


        datalistforfilter = arrayListOf()

        datalistforfilter.add(FilterHomeDataModel("All"))
        datalistforfilter.add(FilterHomeDataModel("Electronics"))
        datalistforfilter.add(FilterHomeDataModel("Books"))
        datalistforfilter.add(FilterHomeDataModel("Vehicles"))
        datalistforfilter.add(FilterHomeDataModel("Clothes"))
        datalistforfilter.add(FilterHomeDataModel("Others"))

        val adapterfilter = HomeFilterAdapter(datalistforfilter)
        recylerViewfilter.adapter = adapterfilter


        // for items
        val layoutManager = GridLayoutManager(context, 2)
        recylerView = view.findViewById(R.id.Home_rc)
        recylerView.layoutManager = layoutManager

        searchView = view.findViewById(R.id.searchView)

        datalist = arrayListOf()
        searchList = arrayListOf()
        datalistforfilteredmyAdapter = arrayListOf()

        datalistforfilteredmyAdapter.addAll(datalist)

        getUserData()

        //To hide floating action button
        recylerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val fab = view.findViewById<ExtendedFloatingActionButton>(R.id.sell_button)
                if (dy > 10 && fab.isShown) {
                    fab.hide()
                }
                if (dy < -10 && !fab.isShown) {
                    fab.show()
                }

                val goToTop=view.findViewById<ExtendedFloatingActionButton>(R.id.top_scroll_button)

                if(dy<-10 && goToTop.isShown){
                    goToTop.hide()
                }
                if(dy>10 && !goToTop.isShown){
                    goToTop.show()
                }

            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val isScrollStopped=recyclerView.scrollState==RecyclerView.SCROLL_STATE_IDLE
                        if(goToTopButton.isShown && isScrollStopped )
                        {
                            goToTopButton.hide()
                        }

                    }, 1000)
                }


            }
        })


        //for searching


        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchList.clear()
                val searchText = p0!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    datalist.forEach {
                        if (it.productName?.toLowerCase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
                            searchList.add(it)
                        }
                    }

                } else {

                    searchList.clear()
                    searchList.addAll(datalist)
                    recylerView.adapter?.notifyDataSetChanged()
                }
                return false
            }


        })

        //koi filter ko press karega toh kya hoga uska code hai

        adapterfilter.setOnItemClickListener(object : HomeFilterAdapter.onItemClickListener {
            override fun onItemClick(category: String) {
                defaultFilter=category
                filterItemClick(category)
            }


        }


        )
        //adding swipe refresh layout
        val swipe: SwipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        swipe.setOnRefreshListener {
            if (CheckInternet.isConnectedToInternet(requireContext())) {
                Toast.makeText(
                   context, "Couldn't refresh! Check your network...",
                    Toast.LENGTH_LONG
                ).show()

            }
            else
            {
                datalist.clear()
                searchList.clear()
                datalistforfilteredmyAdapter.clear()
                recylerView.adapter?.notifyDataSetChanged()
                getUserData()
            }


            swipe.isRefreshing = false
            //Toast.makeText(context,"Home Refreshed", Toast.LENGTH_LONG).show()
        }
    }
    private fun filterItemClick(category:String){
        datalistforfilteredmyAdapter.clear()
        datalist.forEach {
            if (it.category == category) {
                datalistforfilteredmyAdapter.add(it)
            }
        }
        if (category == "All") {
            datalistforfilteredmyAdapter.addAll(datalist)
        }

        if(datalistforfilteredmyAdapter.isEmpty()){

            emptyH.visibility = View.VISIBLE
        }
        else{

            emptyH.visibility = View.INVISIBLE
        }


        recylerView.adapter?.notifyDataSetChanged()
        recylerView.adapter =
            context?.let { HomeItemAdapter(it, this@HomeFragment, datalistforfilteredmyAdapter) }



        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchList.clear()
                val searchText = p0!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    datalistforfilteredmyAdapter.forEach {
                        if (it.productName?.toLowerCase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
                            searchList.add(it)
                        }
                    }
                    recylerView.adapter?.notifyDataSetChanged()
                    recylerView.adapter =
                        context?.let { HomeItemAdapter(it, this@HomeFragment, searchList) }
                } else {
                    searchList.clear()
                    searchList.addAll(datalistforfilteredmyAdapter)
                    recylerView.adapter?.notifyDataSetChanged()
                    recylerView.adapter =
                        context?.let { HomeItemAdapter(it, this@HomeFragment, searchList) }
                }
                return false
            }


        })
    }
    private fun getUserData() {

        searchView.setQuery("", false)
        dbref =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Items")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                loadingAnimationControl.visibility=View.GONE
                if (snapshot.exists()) {
                    datalist.clear()
                    for (userSnapshot in snapshot.children) {

                        val items = userSnapshot.getValue(ItemsHomeDataModel::class.java)
                        if (items != null) {
                            if (!items.sold) {
                                datalist.add(items!!)
                                datalist.forEach{
                                    it.spid = it.pid?.substringAfterLast("_ug")
                                }
                                datalist.sortBy { it.spid }
                            }
                        }
                    }


                    searchList.clear()
                    searchList.addAll(datalist)

                    if(searchList.isEmpty())
                        emptyH.visibility = View.VISIBLE
                    else
                        emptyH.visibility = View.INVISIBLE

                    recylerView.adapter =
                        context?.let { HomeItemAdapter(it, this@HomeFragment, searchList) }
                    filterItemClick(defaultFilter)



                }

            }

            override fun onCancelled(error: DatabaseError) {

                val text = "Error"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            }

        })


    }

}
