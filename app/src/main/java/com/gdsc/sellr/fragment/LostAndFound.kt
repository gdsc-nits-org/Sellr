package com.gdsc.sellr.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gdsc.sellr.LostAndFoundInput
import com.gdsc.sellr.R
import com.gdsc.sellr.adapters.LostAndFoundAdapter
import com.gdsc.sellr.data.LostAndFoundData
import com.gdsc.sellr.databinding.FragmentLostAndFoundBinding
import com.gdsc.sellr.utils.CheckInternet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LostAndFound : Fragment() {


    lateinit var binding :FragmentLostAndFoundBinding
    private lateinit var emptyH : ConstraintLayout
    private var database : FirebaseDatabase ? = null
    lateinit var objectList: ArrayList<LostAndFoundData>
    lateinit var foundList : ArrayList<LostAndFoundData>
    lateinit var lostList : ArrayList<LostAndFoundData>
    lateinit var refreshLostAndFound : SwipeRefreshLayout
    private lateinit var goToTopButton: ExtendedFloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentLostAndFoundBinding.inflate(layoutInflater)
        val listit = binding.lostandfoundFab
        getActivity()?.setTitle("Lost And Found");
        listit.setOnClickListener {
            val lostandfoundInput = Intent(context, LostAndFoundInput::class.java)
            startActivity(lostandfoundInput)
        }
        goToTopButton=binding.topScrollButton




        binding.lostandfoundRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val fab= binding.lostandfoundFab
                if (dy > 10 && fab.isShown) {
                    fab.hide()
                }
                if (dy < -10 && !fab.isShown) {
                    fab.show()
                }
                if(dy<-10 && goToTopButton.isShown){
                    goToTopButton.hide()
                }
                if(dy>10 && !goToTopButton.isShown){
                    goToTopButton.show()
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val isScrollStopped=binding.lostandfoundRecycler.scrollState==RecyclerView.SCROLL_STATE_IDLE
                        if(goToTopButton.isShown && isScrollStopped )
                        {
                            goToTopButton.hide()
                        }

                    }, 1000)
                }


            }
        })

        emptyH = binding.emptyhome
        refreshLostAndFound = binding.lostandfoundSwipeRefresh
        database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        objectList = ArrayList()
        lostList = ArrayList()
        foundList = ArrayList()


        database!!.reference.child("LostAndFound")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    objectList.clear()
                    foundList.clear()
                    lostList.clear()

                    binding.loadingAnimation.visibility=View.GONE
                    binding.lostandfoundFab.visibility=View.VISIBLE

                    for(snapshot1 in snapshot.children){
                        val obj = snapshot1.getValue(LostAndFoundData::class.java)
                        if (obj != null) {
                            objectList.add(obj)
                        }

                        if (obj?.lostOrFound == "FOUND") {
                            foundList.add(obj)
                        }

                        else if (obj?.lostOrFound == "LOST") {
                            lostList.add(obj)
                        }
                    }
                    if(objectList.isEmpty())
                        emptyH.visibility = View.VISIBLE
                    else
                        emptyH.visibility = View.GONE

                    objectList.sortBy {
                        it.dateAdded
                    }
                    foundList.sortBy {
                        it.dateAdded
                    }
                    lostList.sortBy {
                        it.dateAdded
                    }
//                    for(i in objectList)
//                    {
//                        println(i.objectName)
//                    }



                    binding.lostandfoundRecycler.adapter =
                        context?.let { LostAndFoundAdapter(it,objectList) }
                    goToTopButton.setOnClickListener {
                        binding.lostandfoundRecycler.smoothScrollToPosition(0)
                    }
                    binding.lostandfoundFilterFound.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))
                    binding.filterlost.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))
                    binding.lostandfoundFilterAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FEC202"))

                    refreshLostAndFound.setOnRefreshListener {
                        if (requireContext()?.let { CheckInternet.isConnectedToInternet(it) }) {
                            Toast.makeText(
                                context, "Couldn't refresh! Check your network...",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                        else
                        {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()

                            binding.lostandfoundRecycler.adapter =
                                context?.let { LostAndFoundAdapter(it,objectList) }
                        }

                        refreshLostAndFound.isRefreshing = false

                    }

                    binding.lostandfoundFilterFound.setOnClickListener{
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter =
                            context?.let { it1 -> LostAndFoundAdapter(it1,foundList) }


                        binding.lostandfoundFilterFound.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#23ba29"))
                        binding.filterlost.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))
                        binding.lostandfoundFilterAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))

                        refreshLostAndFound.setOnRefreshListener {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                            binding.lostandfoundRecycler.adapter =
                                context?.let { it1 -> LostAndFoundAdapter(it1,foundList) }
                            refreshLostAndFound.isRefreshing = false
                        }

                    }
                    binding.filterlost.setOnClickListener {
                        binding.filterlost.isSelected != binding.filterlost.isSelected


                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter =
                            context?.let { it1 -> LostAndFoundAdapter(it1,lostList) }


                        binding.lostandfoundFilterFound.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))
                        binding.filterlost.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F44336"))
                        binding.lostandfoundFilterAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))

                        refreshLostAndFound.setOnRefreshListener {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                            binding.lostandfoundRecycler.adapter =
                                context?.let { it1 -> LostAndFoundAdapter(it1,lostList) }
                            refreshLostAndFound.isRefreshing = false
                        }
                    }
                    binding.lostandfoundFilterAll.setOnClickListener {
                        binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                        binding.lostandfoundRecycler.adapter =
                            context?.let { it1 -> LostAndFoundAdapter(it1,objectList) }

                        binding.lostandfoundFilterFound.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))
                        binding.filterlost.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#d1cfcf"))
                        binding.lostandfoundFilterAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FEC202"))


                        refreshLostAndFound.setOnRefreshListener {
                            binding.lostandfoundRecycler.adapter?.notifyDataSetChanged()
                            binding.lostandfoundRecycler.adapter =
                                context?.let { it1 -> LostAndFoundAdapter(it1,objectList) }
                            refreshLostAndFound.isRefreshing = false
                        }
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return binding.root
    }


}