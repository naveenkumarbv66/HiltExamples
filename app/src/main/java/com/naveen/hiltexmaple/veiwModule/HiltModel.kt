package com.naveen.hiltexmaple.veiwModule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import jakarta.inject.Named

@InstallIn(ViewModelComponent::class)
@Module
object HiltModel {

    @Provides
    fun provideViewModelData(): DemoViewModelData = DemoViewModelData(".... Naveen .....")

    @Named("addressOne")
    @Provides
    fun getAddressOne() = "Bangalore"

    @Named("addressTwo")
    @Provides
    fun getAddressTwo() = "Mysore"

}