package com.naveen.hiltexmaple.runTime

import com.naveen.hiltexmaple.veiwModule.DemoViewModelData
import org.junit.Test
import org.junit.Assert.*

class AssistedViewModelTest {

    @Test
    fun `AssistedViewModel should create with correct parameters`() {
        // Given
        val location = "Test Location"
        val demoData = DemoViewModelData("Test User")
        val assistedViewModel = AssistedViewModel(location, demoData)

        // Then
        assertNotNull(assistedViewModel)
    }

    @Test
    fun `AssistedViewModel should return correct user info`() {
        // Given
        val location = "Mysore"
        val demoData = DemoViewModelData("Naveen")
        val assistedViewModel = AssistedViewModel(location, demoData)

        // When
        val result = assistedViewModel.getUserInfo()

        // Then
        assertEquals("Naveen ===> Mysore", result)
    }

    @Test
    fun `AssistedViewModel should handle different locations`() {
        // Given
        val location1 = "Bangalore"
        val location2 = "Delhi"
        val demoData = DemoViewModelData("Test User")
        
        val assistedViewModel1 = AssistedViewModel(location1, demoData)
        val assistedViewModel2 = AssistedViewModel(location2, demoData)

        // When
        val result1 = assistedViewModel1.getUserInfo()
        val result2 = assistedViewModel2.getUserInfo()

        // Then
        assertEquals("Test User ===> Bangalore", result1)
        assertEquals("Test User ===> Delhi", result2)
    }

    @Test
    fun `AssistedViewModel should handle different user names`() {
        // Given
        val location = "Mysore"
        val demoData1 = DemoViewModelData("John")
        val demoData2 = DemoViewModelData("Jane")
        
        val assistedViewModel1 = AssistedViewModel(location, demoData1)
        val assistedViewModel2 = AssistedViewModel(location, demoData2)

        // When
        val result1 = assistedViewModel1.getUserInfo()
        val result2 = assistedViewModel2.getUserInfo()

        // Then
        assertEquals("John ===> Mysore", result1)
        assertEquals("Jane ===> Mysore", result2)
    }

    @Test
    fun `AssistedViewModel should handle empty location`() {
        // Given
        val location = ""
        val demoData = DemoViewModelData("Test User")
        val assistedViewModel = AssistedViewModel(location, demoData)

        // When
        val result = assistedViewModel.getUserInfo()

        // Then
        assertEquals("Test User ===> ", result)
    }

    @Test
    fun `AssistedViewModel should handle empty user name`() {
        // Given
        val location = "Test Location"
        val demoData = DemoViewModelData("")
        val assistedViewModel = AssistedViewModel(location, demoData)

        // When
        val result = assistedViewModel.getUserInfo()

        // Then
        assertEquals(" ===> Test Location", result)
    }
}
