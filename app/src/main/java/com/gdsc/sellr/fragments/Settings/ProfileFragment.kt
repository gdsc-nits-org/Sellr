package com.gdsc.sellr.fragments.Settings

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gdsc.sellr.*
import com.gdsc.sellr.databinding.FragmentProfileBinding
import com.gdsc.sellr.viewModels.settings.ProfileViewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var viewBinding: FragmentProfileBinding? = null
    private val binding get() = viewBinding!!

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
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        if (isNetworkConnected(requireContext())) {
            viewModel.retrieveDataFromDatabase()
            observeUserData()
        } else {
            binding.progressBar.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun observeUserData() {
        viewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.EMAIL.text = it.email
                binding.USERNAME.text = it.name
                binding.SCHOLAR.text = it.scholarid
                binding.PHONE.text = it.phonenum
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onResume() {
        super.onResume()
        if (isNetworkConnected(requireContext())) {
            viewModel.retrieveDataFromDatabase()
        }
    }
}
