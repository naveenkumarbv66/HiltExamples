package com.naveen.hiltexmaple.veiwModule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MyViewModelWithPara @Inject constructor(
    demoViewModelData: DemoViewModelData,
    @Named("addressOne") address : String
) : ViewModel() {

    val stateFlowDemoViewModelData = MutableStateFlow(DemoViewModelData(name = ""))

    val stateFlowAddress = MutableStateFlow("")

    init {
        stateFlowDemoViewModelData.update {
            demoViewModelData // Variable coming from the constructor.
        }

        stateFlowAddress.update {
            address // Variable coming from the constructor.

        }
    }

}