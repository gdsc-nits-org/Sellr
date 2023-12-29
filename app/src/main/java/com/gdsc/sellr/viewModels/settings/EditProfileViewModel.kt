// EditProfileViewModel.kt
package com.gdsc.sellr.fragments.Settings

import androidx.lifecycle.ViewModel
import com.gdsc.sellr.dataModels.UserDataModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class EditProfileViewModel : ViewModel() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val myReference: DatabaseReference = database.reference.child("Users")
    private var valueEventListener: ValueEventListener? = null

    fun retrieveUserData(onDataReceived: (UserDataModel) -> Unit) {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.child(Firebase.auth.currentUser?.uid.toString()).getValue(UserDataModel::class.java)
                user?.let { onDataReceived(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        }

        myReference.addValueEventListener(valueEventListener!!)
    }

    fun updateUserData(updatedName: String, updatedPhoneNumber: String, updatedScholarId: String, onComplete: (Boolean) -> Unit) {
        val userId = Firebase.auth.currentUser?.uid.toString()
        val userMap = mutableMapOf<String, Any>()
        userMap["name"] = updatedName
        userMap["phonenum"] = updatedPhoneNumber
        userMap["scholarid"] = updatedScholarId
        myReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    public override fun onCleared() {
        super.onCleared()
        valueEventListener?.let { myReference.removeEventListener(it) }
    }
}
