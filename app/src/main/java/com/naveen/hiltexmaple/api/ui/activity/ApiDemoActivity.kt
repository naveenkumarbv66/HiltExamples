package com.naveen.hiltexmaple.api.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.naveen.hiltexmaple.api.data.model.User
import com.naveen.hiltexmaple.api.ui.viewmodel.UserViewModel
import com.naveen.hiltexmaple.ui.theme.HiltExmapleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApiDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HiltExmapleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ApiDemoScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiDemoScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val usersState by viewModel.usersState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val createUserState by viewModel.createUserState.collectAsState()
    val updateUserState by viewModel.updateUserState.collectAsState()
    val deleteUserState by viewModel.deleteUserState.collectAsState()
    val searchUsersState by viewModel.searchUsersState.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    // Form fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var searchName by remember { mutableStateOf("") }
    var searchEmail by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "API Demo - CRUD Operations",
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
                onClick = { viewModel.loadUsers() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Refresh")
            }

            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Create")
            }

            Button(
                onClick = { showSearchDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Search")
            }
        }

        // Get User by ID Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Get User by ID",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("User ID") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { 
                            userId.toIntOrNull()?.let { id ->
                                viewModel.getUserById(id)
                            }
                        }
                    ) {
                        Text("Get User")
                    }
                }

                // Single User Result
                userState.data?.let { user ->
                    Spacer(modifier = Modifier.height(8.dp))
                    UserCard(user = user, viewModel = viewModel)
                }

                userState.error?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Users List
        when {
            usersState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            usersState.error != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${usersState.error}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        if (usersState.errorCode != null) {
                            Text(
                                text = "Error Code: ${usersState.errorCode}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            usersState.data != null -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(usersState.data!!) { user ->
                        UserCard(
                            user = user,
                            viewModel = viewModel,
                            onEditClick = {
                                selectedUser = user
                                name = user.name
                                email = user.email
                                phone = user.phone ?: ""
                                website = user.website ?: ""
                                showUpdateDialog = true
                            }
                        )
                    }
                }
            }
        }

        // Create User Dialog
        if (showCreateDialog) {
            CreateUserDialog(
                name = name,
                email = email,
                phone = phone,
                website = website,
                onNameChange = { name = it },
                onEmailChange = { email = it },
                onPhoneChange = { phone = it },
                onWebsiteChange = { website = it },
                onDismiss = { showCreateDialog = false },
                onConfirm = {
                    val newUser = User(
                        name = name,
                        email = email,
                        phone = phone.ifEmpty { null },
                        website = website.ifEmpty { null }
                    )
                    viewModel.createUser(newUser)
                    showCreateDialog = false
                    name = ""
                    email = ""
                    phone = ""
                    website = ""
                },
                isLoading = createUserState.isLoading
            )
        }

        // Update User Dialog
        if (showUpdateDialog && selectedUser != null) {
            UpdateUserDialog(
                user = selectedUser!!,
                name = name,
                email = email,
                phone = phone,
                website = website,
                onNameChange = { name = it },
                onEmailChange = { email = it },
                onPhoneChange = { phone = it },
                onWebsiteChange = { website = it },
                onDismiss = { 
                    showUpdateDialog = false
                    selectedUser = null
                },
                onConfirm = {
                    val updatedUser = selectedUser!!.copy(
                        name = name,
                        email = email,
                        phone = phone.ifEmpty { null },
                        website = website.ifEmpty { null }
                    )
                    viewModel.updateUser(selectedUser!!.id!!, updatedUser)
                    showUpdateDialog = false
                    selectedUser = null
                },
                isLoading = updateUserState.isLoading
            )
        }

        // Search Users Dialog
        if (showSearchDialog) {
            SearchUsersDialog(
                name = searchName,
                email = searchEmail,
                onNameChange = { searchName = it },
                onEmailChange = { searchEmail = it },
                onDismiss = { showSearchDialog = false },
                onSearch = {
                    viewModel.searchUsers(
                        name = searchName.ifEmpty { null },
                        email = searchEmail.ifEmpty { null },
                        limit = 10
                    )
                    showSearchDialog = false
                }
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    viewModel: UserViewModel,
    onEditClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    user.website?.let { website ->
                        Text(
                            text = website,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Row {
                    onEditClick?.let {
                        IconButton(onClick = it) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                    IconButton(
                        onClick = { user.id?.let { id -> viewModel.deleteUser(id) } }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun CreateUserDialog(
    name: String,
    email: String,
    phone: String,
    website: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onWebsiteChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New User") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = onPhoneChange,
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = website,
                    onValueChange = onWebsiteChange,
                    label = { Text("Website") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading && name.isNotBlank() && email.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Create")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun UpdateUserDialog(
    user: User,
    name: String,
    email: String,
    phone: String,
    website: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onWebsiteChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update User") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = onPhoneChange,
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = website,
                    onValueChange = onWebsiteChange,
                    label = { Text("Website") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading && name.isNotBlank() && email.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Update")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SearchUsersDialog(
    name: String,
    email: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSearch: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Search Users") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onSearch) {
                Text("Search")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ApiDemoScreenPreview() {
    HiltExmapleTheme {
        ApiDemoScreen()
    }
}
