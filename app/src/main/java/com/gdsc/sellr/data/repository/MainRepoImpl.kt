package com.gdsc.sellr.data.repository

import android.app.Application
import android.util.Log
import com.bumptech.glide.Glide.init
import com.gdsc.sellr.model.Sell
import com.gdsc.sellr.util.Resource
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepoImpl @Inject constructor(
    private val dbitems: DatabaseReference,
    @ApplicationContext private val application: Application
) :
    MainRepo{
    private val TAG = "MainRepoImpl"

    //get instance of MainRepositoryImpl

    companion object {
        @Volatile
        private var instance: MainRepoImpl? = null
        fun getInstance(
            dbitems: DatabaseReference,
            application: Application
        ) = instance ?: synchronized(this) {
            instance ?: MainRepoImpl(dbitems, application).also { instance = it }
        }

    }


    override suspend fun getAllSells(result: (Resource<List<Sell>>) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val snapshot = dbitems.get().await()
                val datalist = mutableListOf<Sell>()

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val items = userSnapshot.getValue(Sell::class.java)
                        if (items != null && !items.sold!!) {
                            datalist.add(items)
                        }
                    }
                }


                datalist.sortBy { it.userUID}

                Resource.Success(datalist)
            } catch (e: Exception) {
                Log.d(TAG, "exception ${e.message}")
                result.invoke(Resource.Error(e.message.toString()))
            }
        }
    }
    }

