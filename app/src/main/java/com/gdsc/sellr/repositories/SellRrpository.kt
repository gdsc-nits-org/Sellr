package com.gdsc.sellr.repository

import com.gdsc.sellr.dataModels.SellDataModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class SellRepository {

    suspend fun uploadSellData(sellDataModel: SellDataModel, userUID: String, uID: String): Boolean {
        try {
            val database = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Items")

            database.child(uID).setValue(sellDataModel).await()

            val userDatabase = FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users")

            userDatabase.child(userUID).child("pId").push().setValue(uID).await()

            return true
        } catch (e: Exception) {
            return false
        }
    }
}
