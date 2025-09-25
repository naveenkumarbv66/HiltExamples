package com.naveen.hiltexmaple.usingInterface

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class InterfaceModule {

    @Binds
    abstract fun bindInterface(interfaceImple: InterfaceImple): TestInterface
}