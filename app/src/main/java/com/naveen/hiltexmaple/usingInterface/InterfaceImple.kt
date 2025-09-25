package com.naveen.hiltexmaple.usingInterface

import jakarta.inject.Inject

class InterfaceImple @Inject constructor() : TestInterface {

    override fun printSomeThing(): String {
        return "Hello Its binding interface exmple."
    }
}