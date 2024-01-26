package com.gdsc.sellr.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdsc.sellr.dataModels.SellDataModel
import com.gdsc.sellr.repository.SellRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SellViewModel(application: Application) : AndroidViewModel(application) {

        private val sellRepository = SellRepository()

        private val _progressBarVisible = MutableStateFlow(false)
        val progressBarVisible: StateFlow<Boolean>
                get() = _progressBarVisible

        suspend fun uploadSellData(sellDataModel: SellDataModel, userUID: String, uID: String): Boolean {
                setProgressBar()

                return try {
                        val success = sellRepository.uploadSellData(sellDataModel, userUID, uID)
                        if (success) {
                                makeToast("Successfully Listed")
                        } else {
                                makeToast("Listing Failed, Please try again")
                        }
                        deleteProgressBar()
                        success
                } catch (e: Exception) {
                        makeToast("Listing Failed, Please try again")
                        deleteProgressBar()
                        false
                }
        }

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
