package com.example.realestateapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonenew.repositories.DataRepository
import com.example.realestateapp.models.Phone
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PhoneDetailViewModel(private val repository: DataRepository) : ViewModel() {
    private val _phoneDetails = MutableLiveData<Phone>()
    val phoneDetails: LiveData<Phone> get() = _phoneDetails

    fun fetchPhoneDetails(id: Int) {
        viewModelScope.launch {
            try {
                val phone = repository.getPhoneDetails(id)
                _phoneDetails.postValue(phone)
            } catch (e: HttpException) {
                // Maneja el error aquí
                println("Error: ${e.message}")
            }
        }
    }
}
