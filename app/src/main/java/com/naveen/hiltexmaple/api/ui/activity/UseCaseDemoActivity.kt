package com.naveen.hiltexmaple.api.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.usecase.UserUseCase
import com.naveen.hiltexmaple.api.domain.usecase.UserStatistics
import com.naveen.hiltexmaple.ui.theme.HiltExmapleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Activity demonstrating Use Case pattern with direct injection
 * This shows how to use use cases directly in Activities
 */
@AndroidEntryPoint
class UseCaseDemoActivity : ComponentActivity() {

    @Inject
    lateinit var userUseCase: UserUseCase

    private var users by mutableStateOf<List<User>>(emptyList())
    private var isLoading by mutableStateOf(false)
    private var errorMessage by mutableStateOf<String?>(null)
    private var statistics by mutableStateOf<UserStatistics?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HiltExmapleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UseCaseDemoScreen()
                }
            }
        }
        
        // Load initial data
        loadUsers()
        loadStatistics()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            isLoading = true
            errorMessage = null
            
            userUseCase.getAllUsers().collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        isLoading = true
                        errorMessage = null
                    }
                    is ApiResult.Success -> {
                        isLoading = false
                        users = result.data
                        errorMessage = null
                    }
                    is ApiResult.Error -> {
                        isLoading = false
                        errorMessage = result.message
                    }
                }
            }
        }
    }

    private fun loadStatistics() {
        lifecycleScope.launch {
            userUseCase.getUserStatistics().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        statistics = result.data
                    }
                    is ApiResult.Error -> {
                        // Handle error silently for statistics
                    }
                    is ApiResult.Loading -> {
                        // Handle loading state
                    }
                }
            }
        }
    }

    private fun createUser() {
        lifecycleScope.launch {
            val newUser = User(
                name = "New User ${System.currentTimeMillis()}",
                email = "user${System.currentTimeMillis()}@example.com"
            )
            
            userUseCase.createUser(newUser).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        loadUsers() // Refresh the list
                        loadStatistics() // Refresh statistics
                    }
                    is ApiResult.Error -> {
                        errorMessage = result.message
                    }
                    is ApiResult.Loading -> {
                        isLoading = true
                    }
                }
            }
        }
    }

    private fun deleteUser(user: User) {
        user.id?.let { userId ->
            lifecycleScope.launch {
                userUseCase.deleteUser(userId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            loadUsers() // Refresh the list
                            loadStatistics() // Refresh statistics
                        }
                        is ApiResult.Error -> {
                            errorMessage = result.message
                        }
                        is ApiResult.Loading -> {
                            isLoading = true
                        }
                    }
                }
            }
        }
    }

    private fun searchUsers(query: String) {
        lifecycleScope.launch {
            userUseCase.searchUsers(name = query, email = null, limit = 5).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        users = result.data
                        errorMessage = null
                    }
                    is ApiResult.Error -> {
                        errorMessage = result.message
                    }
                    is ApiResult.Loading -> {
                        isLoading = true
                    }
                }
            }
        }
    }

    @Composable
    fun UseCaseDemoScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Use Case Demo - Activity Usage",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Statistics Card
            statistics?.let { stats ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "User Statistics",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total Users: ${stats.totalUsers}")
                        Text("With Email: ${stats.usersWithEmail}")
                        Text("With Phone: ${stats.usersWithPhone}")
                        Text("With Website: ${stats.usersWithWebsite}")
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { loadUsers() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Refresh")
                }

                Button(
                    onClick = { createUser() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Create")
                }
            }

            // Error Message
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Loading Indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Users List
            if (users.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        UserCard(
                            user = user,
                            onDeleteClick = { deleteUser(user) }
                        )
                    }
                }
            } else if (!isLoading && errorMessage == null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No users found",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @Composable
    fun UserCard(
        user: User,
        onDeleteClick: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    user.phone?.let { phone ->
                        Text(
                            text = phone,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
