package com.example.sellr.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sellr.*
import com.example.sellr.R
import com.example.sellr.data.UserData
import com.example.sellr.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    val database: FirebaseDatabase =FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myReference:DatabaseReference=database.reference.child("Users")
    private var viewBinding: FragmentProfileBinding?=null
    private val binding get()= viewBinding!!

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                val i = Intent(context, MainFragmentHolder::class.java)
                i.putExtra("editProfile", "editProfile")
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding=FragmentProfileBinding.inflate(inflater,container,false)
        val view=binding.root

        if (isNetworkConnected(requireContext())) {
            retriveDataFromDatabase()
        } else {

            binding.progressBar.visibility=View.VISIBLE

            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
        }


        return view
    }
    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    fun retriveDataFromDatabase() {
        val startTime = System.currentTimeMillis()

        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.visibility = View.VISIBLE

                val user = snapshot.child(Firebase.auth.currentUser?.uid.toString()).getValue(UserData::class.java)
                if (user != null) {
                    binding.EMAIL.text = user.email
                    binding.USERNAME.text = user.name
                    binding.SCHOLAR.text = user.scholarid
                    binding.PHONE.text = user.phonenum
                    binding.progressBar.visibility = View.GONE
                }

//                val endTime = System.currentTimeMillis()
//                val duration = endTime - startTime

//                if (duration > 5000) {
//                    Toast.makeText(requireContext(), "Slow Internet Connection", Toast.LENGTH_LONG).show()
//                } else {
//                    viewBinding?.progressBar?.visibility = View.GONE
//                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}