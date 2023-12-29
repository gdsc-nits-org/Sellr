package com.gdsc.sellr.viewModels.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.sellr.dataModels.UserDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {
    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val myReference: DatabaseReference = database.reference.child("Users")

    private val _userData = MutableLiveData<UserDataModel?>()
    val userData: MutableLiveData<UserDataModel?>
        get() = _userData

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val user =
                snapshot.child(Firebase.auth.currentUser?.uid.toString())
                    .getValue(UserDataModel::class.java)
            _userData.value = user
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle onCancelled event if needed
        }
    }

    fun retrieveDataFromDatabase() {
        myReference.addValueEventListener(valueEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        myReference.removeEventListener(valueEventListener)
    }
}
