package com.gdsc.sellr.viewModels

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdsc.sellr.dataModels.SellDataModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SellViewModel(application: Application) : AndroidViewModel(application) {

private val _sellDataModel = MutableStateFlow<SellDataModel?>(null)

        fun uploadSellData(
        sellDataModel: SellDataModel,
        userUID: String,
        uID: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
        ) {
        setProgressBar()

        val database =
        FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        .getReference("Items")

        val uploadData = database.child(uID).setValue(sellDataModel)
        Handler(Looper.getMainLooper()).postDelayed({
        if (_sellDataModel.value != null) {
        Toast.makeText(
        getApplication(),
        "Listing Failed, Please try again",
        Toast.LENGTH_LONG
        ).show()
        deleteProgressBar()
        FirebaseDatabase.getInstance().purgeOutstandingWrites()
        onFailure.invoke()
        }
        }, 180000)

        uploadData.addOnSuccessListener {
        val userDatabase =
        FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        .getReference("Users")

        val upDateUserList = userDatabase.child(userUID).child("pId").push().setValue(uID)
        upDateUserList.addOnSuccessListener {
        makeToast("Successfully Listed")
        deleteProgressBar()
        onSuccess.invoke()
        }.addOnFailureListener {
        makeToast("Listing Failed, Please try again")
        deleteProgressBar()
        onFailure.invoke()
        }
        }.addOnFailureListener {
        makeToast("Listing Failed, Please try again")
        deleteProgressBar()
        onFailure.invoke()
        }
        }


private val _progressBarVisible = MutableStateFlow(false)
        val progressBarVisible: StateFlow<Boolean>
        get() = _progressBarVisible

                fun setProgressBar() {
                viewModelScope.launch {
                _progressBarVisible.value = true
                }
                }

                fun deleteProgressBar() {
                viewModelScope.launch {
                _progressBarVisible.value = false
                }
                }

private fun makeToast(value: String) {
        Toast.makeText(getApplication(), value, Toast.LENGTH_LONG).show()
        }



}