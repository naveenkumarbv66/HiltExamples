package com.naveen.hiltexmaple.api.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.domain.model.ApiResult
import com.naveen.hiltexmaple.api.domain.usecase.UserUseCase
import com.naveen.hiltexmaple.ui.theme.HiltExmapleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragment demonstrating Use Case pattern with direct injection
 * This shows how to use use cases directly in Fragments
 */
@AndroidEntryPoint
class UseCaseDemoFragment : Fragment() {

    @Inject
    lateinit var userUseCase: UserUseCase

    private var users by mutableStateOf<List<User>>(emptyList())
    private var isLoading by mutableStateOf(false)
    private var errorMessage by mutableStateOf<String?>(null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HiltExmapleTheme {
                    UseCaseDemoFragmentContent()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUsers()
    }

    private fun loadUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
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
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun createUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newUser = User(
                name = "Fragment User ${System.currentTimeMillis()}",
                email = "fragment${System.currentTimeMillis()}@example.com"
            )
            
            userUseCase.createUser(newUser).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        loadUsers() // Refresh the list
                        Toast.makeText(requireContext(), "User created successfully", Toast.LENGTH_SHORT).show()
                    }
                    is ApiResult.Error -> {
                        errorMessage = result.message
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
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
            viewLifecycleOwner.lifecycleScope.launch {
                userUseCase.deleteUser(userId).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            loadUsers() // Refresh the list
                            Toast.makeText(requireContext(), "User deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        is ApiResult.Error -> {
                            errorMessage = result.message
                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
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
        viewLifecycleOwner.lifecycleScope.launch {
            userUseCase.searchUsers(name = query, email = null, limit = 5).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        users = result.data
                        errorMessage = null
                    }
                    is ApiResult.Error -> {
                        errorMessage = result.message
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    }
                    is ApiResult.Loading -> {
                        isLoading = true
                    }
                }
            }
        }
    }

    @Composable
    fun UseCaseDemoFragmentContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Use Case Demo - Fragment Usage",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
                    Text("No users found")
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
