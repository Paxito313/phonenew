package com.example.phonenew.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonenew.repositories.DataRepository
import com.example.realestateapp.models.Phone
import kotlinx.coroutines.launch

class PhoneListViewModel(private val repository: DataRepository) : ViewModel() {
    val allPhones: LiveData<List<Phone>> = repository.allPhones

    fun refreshPhones() {
        viewModelScope.launch {
            repository.refreshPhones()
        }
    }
}
