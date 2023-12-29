// SoldViewModel.kt
package com.gdsc.sellr.viewModels.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.sellr.dataModels.SellDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SoldViewModel : ViewModel() {

    private val _soldItems = MutableLiveData<List<SellDataModel>>()
    val soldItems: LiveData<List<SellDataModel>> get() = _soldItems

    fun retrieveSoldItemsFromDatabase() {
        val user = Firebase.auth.currentUser?.uid.toString()
        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myReference: DatabaseReference = database.reference.child("Items")

        myReference.get().addOnSuccessListener { dataSnapshot ->
            val soldItemList = mutableListOf<SellDataModel>()

            for (eachItem in dataSnapshot.children) {
                val item = eachItem.getValue(SellDataModel::class.java)
                if (item != null && item.userUID == user && item.sold == true) {
                    soldItemList.add(item)
                }
            }

            _soldItems.value = soldItemList
        }.addOnFailureListener {
            // Handle failure
        }
    }
}
