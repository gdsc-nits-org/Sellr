package com.gdsc.sellr.viewModels.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.sellr.dataModels.SellDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class OnSaleViewModel : ViewModel() {
    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val myReference: DatabaseReference = database.reference.child("Items")

    private val _onSaleItems = MutableLiveData<List<SellDataModel>>()
    val onSaleItems: LiveData<List<SellDataModel>>
        get() = _onSaleItems

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = Firebase.auth.currentUser?.uid.toString()
            val itemList = mutableListOf<SellDataModel>()

            for (eachItem in snapshot.children) {
                val item = eachItem.getValue(SellDataModel::class.java)
                if (item != null && item.userUID == user && !item.sold!!) {
                    itemList.add(item)
                }
            }

            _onSaleItems.value = itemList
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle onCancelled event if needed
        }
    }

    fun retrieveOnSaleItemsFromDatabase() {
        myReference.addValueEventListener(valueEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        myReference.removeEventListener(valueEventListener)
    }
}
