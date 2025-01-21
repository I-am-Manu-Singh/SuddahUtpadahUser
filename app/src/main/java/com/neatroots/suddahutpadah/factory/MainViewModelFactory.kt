package com.neatroots.suddahutpadah.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.neatroots.suddahutpadah.repository.MainRepository
import com.neatroots.suddahutpadah.viewmodel.MainViewModel


class  MainViewModelFactory(private val mainRepository: MainRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}