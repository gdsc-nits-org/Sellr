package com.gdsc.sellr.ui.fragment.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdsc.sellr.data.repository.MainRepo
import com.gdsc.sellr.model.Sell
import com.gdsc.sellr.util.Resource
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: MainRepo
)
    : ViewModel(){

    private val _allSells = MutableLiveData<Resource<List<Sell>>>()
    val allSells = _allSells as LiveData<Resource<List<Sell>>>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading as LiveData<Boolean>

    fun isLoading(isLoading: Boolean){
        _isLoading.value = isLoading
    }


    suspend fun getAllSells(){
        _allSells.value = Resource.Loading
        try {
            repository.getAllSells() {
                _allSells.value = it
            }
        }catch (e : FirebaseException){
            _allSells.value = Resource.Error(e.message.toString())
        }catch (e : Exception){
            _allSells.value = Resource.Error(e.message.toString())
        }
    }


}