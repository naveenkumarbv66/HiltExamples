package com.naveen.hiltexmaple.veiwModule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class ViewModelModuleTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var myViewModel: MyViewModel
    private lateinit var myViewModelWithPara: MyViewModelWithPara

    @Before
    fun setup() {
        // Note: In a real test, you would use HiltTestApplication and inject these
        // For this example, we'll test the functionality directly
        myViewModel = MyViewModel()
        
        // Create test data for MyViewModelWithPara
        val demoData = DemoViewModelData("Test Name")
        myViewModelWithPara = MyViewModelWithPara(demoData, "Test Address")
    }

    @Test
    fun `MyViewModel should return correct message`() {
        // When
        val result = myViewModel.getSomethingFromViewModel()

        // Then
        assertEquals("Hello from ViewModel", result)
    }

    @Test
    fun `MyViewModelWithPara should initialize with correct data`() {
        // Then
        assertEquals("Test Name", myViewModelWithPara.stateFlowDemoViewModelData.value.name)
        assertEquals("Test Address", myViewModelWithPara.stateFlowAddress.value)
    }

    @Test
    fun `MyViewModelWithPara should update state flows correctly`() {
        // Given
        val newData = DemoViewModelData("Updated Name")
        val newAddress = "Updated Address"

        // When
        myViewModelWithPara.stateFlowDemoViewModelData.value = newData
        myViewModelWithPara.stateFlowAddress.value = newAddress

        // Then
        assertEquals("Updated Name", myViewModelWithPara.stateFlowDemoViewModelData.value.name)
        assertEquals("Updated Address", myViewModelWithPara.stateFlowAddress.value)
    }

    @Test
    fun `DemoViewModelData should work correctly`() {
        // Given
        val data = DemoViewModelData("Test Name")

        // Then
        assertEquals("Test Name", data.name)
    }

    @Test
    fun `DemoViewModelData should support copy`() {
        // Given
        val originalData = DemoViewModelData("Original Name")

        // When
        val copiedData = originalData.copy(name = "Copied Name")

        // Then
        assertEquals("Original Name", originalData.name)
        assertEquals("Copied Name", copiedData.name)
    }

    @Test
    fun `DemoViewModelData should support equality`() {
        // Given
        val data1 = DemoViewModelData("Test Name")
        val data2 = DemoViewModelData("Test Name")
        val data3 = DemoViewModelData("Different Name")

        // Then
        assertEquals(data1, data2)
        assertNotEquals(data1, data3)
        assertEquals(data1.hashCode(), data2.hashCode())
    }
}
