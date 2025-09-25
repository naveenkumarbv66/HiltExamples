package com.naveen.hiltexmaple.veiwModule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(): ViewModel() {

    fun getSomethingFromViewModel(): String{
        return "Hello from ViewModel"
    }
}