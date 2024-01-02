package com.gdsc.sellr.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdsc.sellr.dataModels.SellDataModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SellViewModel(application: Application) : AndroidViewModel(application) {

        private val _sellDataModel = MutableStateFlow<SellDataModel?>(null)

        suspend fun uploadSellData(
                sellDataModel: SellDataModel,
                userUID: String,
                uID: String
        ): Boolean {
                setProgressBar()

                return try {
                        val database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Items")

                        database.child(uID).setValue(sellDataModel).await()

                        val userDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Users")

                        userDatabase.child(userUID).child("pId").push().setValue(uID).await()

                        makeToast("Successfully Listed")
                        deleteProgressBar()
                        true
                } catch (e: Exception) {
                        makeToast("Listing Failed, Please try again")
                        deleteProgressBar()
                        false
                }
        }


        private val _progressBarVisible = MutableStateFlow(false)
        val progressBarVisible: StateFlow<Boolean>
                get() = _progressBarVisible

        private fun setProgressBar() {
                viewModelScope.launch {
                        _progressBarVisible.value = true
                }
        }

        private fun deleteProgressBar() {
                viewModelScope.launch {
                        _progressBarVisible.value = false
                }
        }

        private fun makeToast(value: String) {
                Toast.makeText(getApplication(), value, Toast.LENGTH_LONG).show()
        }



}