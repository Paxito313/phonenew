package com.example.realestateapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.phonenew.repositories.DataRepository
import com.example.phonenew.viewmodels.PhoneListViewModel

class PhoneListViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PhoneListViewModel::class.java)) {
            PhoneListViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
