package com.naveen.hiltexmaple.runTime

import androidx.lifecycle.ViewModel
import com.naveen.hiltexmaple.veiwModule.DemoViewModelData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@AssistedFactory
interface AssistedViewModelInterface{
    fun create(location: String) : AssistedViewModel
}

@HiltViewModel(assistedFactory = AssistedViewModelInterface::class)
class AssistedViewModel @AssistedInject constructor(
    @Assisted private val location: String,
    private val demoViewModelData: DemoViewModelData
) : ViewModel() {

    fun getUserInfo(): String {
        return demoViewModelData.name.plus(" ===> ").plus(location)
    }
}