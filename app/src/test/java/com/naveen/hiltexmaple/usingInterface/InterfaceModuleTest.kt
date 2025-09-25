package com.naveen.hiltexmaple.usingInterface

import org.junit.Test
import org.junit.Assert.*

class InterfaceModuleTest {

    @Test
    fun `InterfaceImple should implement TestInterface`() {
        // Given
        val interfaceImpl = InterfaceImple()

        // Then
        assertNotNull(interfaceImpl)
        assertTrue(interfaceImpl is TestInterface)
        assertTrue(interfaceImpl is InterfaceImple)
    }

    @Test
    fun `InterfaceImple should return correct message`() {
        // Given
        val interfaceImpl = InterfaceImple()

        // When
        val result = interfaceImpl.printSomeThing()

        // Then
        assertEquals("Hello Its binding interface exmple.", result)
    }

    @Test
    fun `TestInterface should be instantiable`() {
        // Given
        val interfaceImpl = InterfaceImple()

        // Then
        assertNotNull(interfaceImpl)
    }

    @Test
    fun `InterfaceImple should be injectable`() {
        // Given
        val interfaceImpl = InterfaceImple()

        // Then
        assertNotNull(interfaceImpl)
        // This test verifies that the class can be instantiated for injection
    }
}
