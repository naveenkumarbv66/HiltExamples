package com.naveen.hiltexmaple.api.data.model

import org.junit.Test
import org.junit.Assert.*

class UserTest {

    @Test
    fun `User data class should create instance with all properties`() {
        // Given
        val address = Address(
            street = "123 Main St",
            suite = "Apt 4B",
            city = "New York",
            zipcode = "10001",
            geo = Geo(lat = "40.7128", lng = "-74.0060")
        )
        
        val company = Company(
            name = "Test Company",
            catchPhrase = "Test Catch Phrase",
            bs = "Test BS"
        )
        
        val user = User(
            id = 1,
            name = "John Doe",
            email = "john.doe@example.com",
            phone = "123-456-7890",
            website = "johndoe.com",
            address = address,
            company = company
        )

        // Then
        assertEquals(1, user.id)
        assertEquals("John Doe", user.name)
        assertEquals("john.doe@example.com", user.email)
        assertEquals("123-456-7890", user.phone)
        assertEquals("johndoe.com", user.website)
        assertEquals(address, user.address)
        assertEquals(company, user.company)
    }

    @Test
    fun `User data class should create instance with minimal properties`() {
        // Given
        val user = User(
            name = "Jane Doe",
            email = "jane.doe@example.com"
        )

        // Then
        assertNull(user.id)
        assertEquals("Jane Doe", user.name)
        assertEquals("jane.doe@example.com", user.email)
        assertNull(user.phone)
        assertNull(user.website)
        assertNull(user.address)
        assertNull(user.company)
    }

    @Test
    fun `User data class should support equality`() {
        // Given
        val user1 = User(
            id = 1,
            name = "John Doe",
            email = "john.doe@example.com"
        )
        
        val user2 = User(
            id = 1,
            name = "John Doe",
            email = "john.doe@example.com"
        )

        // Then
        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `User data class should support copy`() {
        // Given
        val originalUser = User(
            id = 1,
            name = "John Doe",
            email = "john.doe@example.com"
        )

        // When
        val copiedUser = originalUser.copy(name = "Jane Doe")

        // Then
        assertEquals(1, copiedUser.id)
        assertEquals("Jane Doe", copiedUser.name)
        assertEquals("john.doe@example.com", copiedUser.email)
    }

    @Test
    fun `Address data class should create instance with all properties`() {
        // Given
        val geo = Geo(lat = "40.7128", lng = "-74.0060")
        val address = Address(
            street = "123 Main St",
            suite = "Apt 4B",
            city = "New York",
            zipcode = "10001",
            geo = geo
        )

        // Then
        assertEquals("123 Main St", address.street)
        assertEquals("Apt 4B", address.suite)
        assertEquals("New York", address.city)
        assertEquals("10001", address.zipcode)
        assertEquals(geo, address.geo)
    }

    @Test
    fun `Address data class should create instance with null properties`() {
        // Given
        val address = Address()

        // Then
        assertNull(address.street)
        assertNull(address.suite)
        assertNull(address.city)
        assertNull(address.zipcode)
        assertNull(address.geo)
    }

    @Test
    fun `Geo data class should create instance with coordinates`() {
        // Given
        val geo = Geo(lat = "40.7128", lng = "-74.0060")

        // Then
        assertEquals("40.7128", geo.lat)
        assertEquals("-74.0060", geo.lng)
    }

    @Test
    fun `Company data class should create instance with all properties`() {
        // Given
        val company = Company(
            name = "Test Company",
            catchPhrase = "Test Catch Phrase",
            bs = "Test BS"
        )

        // Then
        assertEquals("Test Company", company.name)
        assertEquals("Test Catch Phrase", company.catchPhrase)
        assertEquals("Test BS", company.bs)
    }

    @Test
    fun `Company data class should create instance with null properties`() {
        // Given
        val company = Company()

        // Then
        assertNull(company.name)
        assertNull(company.catchPhrase)
        assertNull(company.bs)
    }
}
