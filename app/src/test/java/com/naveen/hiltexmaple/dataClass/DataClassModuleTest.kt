package com.naveen.hiltexmaple.dataClass

import com.naveen.hiltexmaple.dataClass.Person
import org.junit.Test
import org.junit.Assert.*

class DataClassModuleTest {

    @Test
    fun `Person should be created with correct values`() {
        // Given
        val person = Person("Naveen", "Bangalore")

        // Then
        assertNotNull(person)
        assertEquals("Naveen", person.name)
        assertEquals("Bangalore", person.address)
    }

    @Test
    fun `Person should be mutable`() {
        // Given
        val person = Person("Naveen", "Bangalore")
        val originalName = person.name
        val originalAddress = person.address

        // When
        person.name = "Updated Name"
        person.address = "Updated Address"

        // Then
        assertEquals("Updated Name", person.name)
        assertEquals("Updated Address", person.address)
        assertNotEquals(originalName, person.name)
        assertNotEquals(originalAddress, person.address)
    }

    @Test
    fun `Person should support equality`() {
        // Given
        val person1 = Person("Naveen", "Bangalore")
        val person2 = Person("Naveen", "Bangalore")
        val person3 = Person("John", "Delhi")

        // Then
        assertEquals(person1, person2)
        assertNotEquals(person1, person3)
        assertEquals(person1.hashCode(), person2.hashCode())
    }

    @Test
    fun `Person should support copy`() {
        // Given
        val originalPerson = Person("Naveen", "Bangalore")

        // When
        val copiedPerson = originalPerson.copy(name = "Updated Name")

        // Then
        assertEquals("Updated Name", copiedPerson.name)
        assertEquals("Bangalore", copiedPerson.address)
        assertEquals("Naveen", originalPerson.name) // Original unchanged
    }
}
