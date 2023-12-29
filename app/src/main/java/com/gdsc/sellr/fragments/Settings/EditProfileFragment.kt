// EditProfileFragment.kt
package com.gdsc.sellr.fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gdsc.sellr.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var viewBinding: FragmentEditProfileBinding? = null
    private val binding get() = viewBinding!!
    private val viewModel: EditProfileViewModel by lazy { EditProfileViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        setData()

        binding.updateButton.setOnClickListener {
            updateData()
        }

        return view
    }

    private fun setData() {
        viewModel.retrieveUserData { user ->
            binding.editUserName.setText(user.name)
            binding.editPhoneNumber.setText(user.phonenum)
            if (user.scholarid == "-------") {
                binding.editScholarID.setText("")
            } else {
                binding.editScholarID.setText(user.scholarid)
            }
        }
    }

    private fun updateData() {
        val updatedName = binding.editUserName.text.toString()
        var updatedScholarId = binding.editScholarID.text.toString()
        val updatedPhoneNumber = binding.editPhoneNumber.text.toString()
        var checkEmpty = false

        if (updatedName.isBlank()) {
            checkEmpty = true
            binding.getUser.error = "This field is required"
        } else {
            binding.getUser.error = null
        }

        if (updatedPhoneNumber.isBlank()) {
            checkEmpty = true
            binding.getPhone.error = "This field is required"
        } else {
            binding.getPhone.error = null
        }

        if (updatedScholarId.isBlank()) {
            updatedScholarId = "-------"
        }

        if (!checkEmpty) {
            viewModel.updateUserData(updatedName, updatedPhoneNumber, updatedScholarId) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(requireContext(), "The user data has been updated", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                } else {
                    // Handle update failure if needed
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onCleared()
    }
}
