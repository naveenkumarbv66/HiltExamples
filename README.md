# Hilt Examples - Android Dependency Injection

A comprehensive Android application demonstrating various Hilt (Dagger) dependency injection patterns and best practices using Jetpack Compose.

## 📱 Overview

This project showcases different ways to implement dependency injection using Google's Hilt framework in an Android application. It includes examples of basic injection, ViewModels, interfaces, data classes, and advanced patterns like assisted injection.

## 🏗️ Project Structure

```
app/src/main/java/com/naveen/hiltexmaple/
├── AppApplication.kt              # Hilt Application class
├── MainActivity.kt               # Main activity with @AndroidEntryPoint
├── dataClass/
│   ├── Person.kt                 # Data class example
│   └── DataClassModule.kt        # Module for data class injection
├── usingInterface/
│   ├── TestInterface.kt          # Interface definition
│   ├── InterfaceImple.kt         # Interface implementation
│   └── InterfaceModule.kt        # Module for interface binding
├── veiwModule/
│   ├── MyViewModel.kt            # Basic ViewModel injection
│   ├── MyViewModelWithPara.kt    # ViewModel with constructor parameters
│   ├── HiltModel.kt              # ViewModel module with @Named providers
│   └── viewModelData.kt          # Data class for ViewModel
├── runTime/
│   └── AssistedViewModel.kt      # Assisted injection example
├── api/                          # NEW: Complete API Implementation
│   ├── data/
│   │   ├── model/
│   │   │   ├── User.kt           # User data model
│   │   │   └── ApiResponse.kt    # API response wrapper
│   │   ├── repository/
│   │   │   └── UserRepositoryImpl.kt  # Repository implementation
│   │   └── remote/
│   │       ├── ApiService.kt     # Retrofit API interface
│   │       └── NetworkException.kt    # Network error handling
│   ├── domain/
│   │   ├── model/
│   │   │   └── UserState.kt      # UI state models
│   │   └── repository/
│   │       └── UserRepository.kt # Repository interface
│   ├── di/
│   │   ├── NetworkModule.kt      # Network dependencies
│   │   └── RepositoryModule.kt   # Repository binding
│   └── ui/
│       ├── viewmodel/
│       │   └── UserViewModel.kt  # ViewModel with StateFlow
│       └── activity/
│           └── ApiDemoActivity.kt # Complete CRUD UI
└── ui/theme/                     # Compose theme files
```

## 🚀 Features Demonstrated

### 1. **Basic Hilt Setup**
- `@HiltAndroidApp` in Application class
- `@AndroidEntryPoint` in Activity
- Basic dependency injection with `@Inject`

### 2. **Data Class Injection**
- Singleton data class injection using `@Provides`
- Module installation in `SingletonComponent`

### 3. **Interface Binding**
- Interface to implementation binding using `@Binds`
- Activity-scoped injection

### 4. **ViewModel Injection**
- Basic ViewModel with `@HiltViewModel`
- ViewModel with constructor parameters
- Named qualifiers using `@Named`

### 5. **Assisted Injection**
- Runtime parameter passing to ViewModels
- `@AssistedFactory` pattern for dynamic dependencies

### 6. **Jetpack Compose Integration**
- Hilt ViewModels in Compose
- StateFlow integration with Compose

### 7. **Complete API Implementation (NEW)**
- **Clean Architecture MVVM** with Retrofit, Coroutines, and Hilt
- **Full CRUD Operations**: GET, POST, PUT, PATCH, DELETE
- **Comprehensive Error Handling**: HTTP errors (400, 401, 403, 404, 422, 429, 500, 502, 503)
- **Network Exception Handling**: Timeout, No Internet, Network errors
- **StateFlow Integration**: Loading, Success, Error states
- **Modern UI**: Jetpack Compose with Material3 design
- **Repository Pattern**: Clean separation of concerns
- **Dependency Injection**: Complete Hilt setup for networking

## 🛠️ Technologies Used

- **Android SDK**: API 24+ (Android 7.0+)
- **Kotlin**: 2.0.21
- **Hilt**: 2.57.1
- **Jetpack Compose**: 2024.09.00
- **Android Gradle Plugin**: 8.11.2
- **Target SDK**: 36

## 📦 Dependencies

### Core Dependencies
```kotlin
// Hilt
implementation("com.google.dagger:hilt-android:2.57.1")
kapt("com.google.dagger:hilt-compiler:2.57.1")

// Compose
implementation("androidx.activity:activity-compose:1.10.1")
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Hilt Navigation Compose
implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

// ViewModel Compose
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")

// NEW: Networking Dependencies
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
implementation("com.google.code.gson:gson:2.11.0")

// NEW: Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
```

## 🔧 Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd HiltExamples
   ```

2. **Open in Android Studio**
   - Ensure you have Android Studio Arctic Fox or later
   - Sync the project with Gradle files

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

## ✅ Build Status

The project builds successfully! You may see some deprecation warnings during compilation, but these are **not errors** and don't affect functionality:

### 📝 Known Warnings (Non-Critical)

1. **Hilt Generated Code Warning**:
   ```
   Note: Hilt_AppApplication.java uses or overrides a deprecated API.
   ```
   - This is from Hilt's internal generated code using deprecated Android APIs
   - **Normal and expected** - it's a framework issue, not your code

2. **Gson Deprecation Warning**:
   ```
   'fun setLenient(): GsonBuilder!' is deprecated
   ```
   - The `setLenient()` method in Gson is deprecated but still functional
   - **Non-critical** - doesn't affect API functionality

3. **Kapt Warning**:
   ```
   Kapt currently doesn't support language version 2.0+. Falling back to 1.9.
   ```
   - Kapt automatically falls back to Kotlin 1.9 for compatibility
   - **Expected behavior** - works perfectly with fallback

### 🎯 What This Means

- ✅ **App compiles successfully**
- ✅ **All API functionality works**
- ✅ **Hilt dependency injection works**
- ✅ **Ready to install and run**

These warnings are **cosmetic** and don't impact the app's functionality or performance.

## 📚 Code Examples

### 1. Application Setup
```kotlin
@HiltAndroidApp
class AppApplication : Application()
```

### 2. Activity with Dependency Injection
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var person: Person
    
    @Inject
    lateinit var testInterface: TestInterface
    
    val myViewModel: MyViewModel by viewModels()
}
```

### 3. Data Class Module
```kotlin
@InstallIn(SingletonComponent::class)
@Module
object DataClassModule {
    @Provides
    @Singleton
    fun providePerson(): Person {
        return Person("Naveen", "Bangalore")
    }
}
```

### 4. Interface Binding
```kotlin
@InstallIn(ActivityComponent::class)
@Module
abstract class InterfaceModule {
    @Binds
    abstract fun bindInterface(interfaceImple: InterfaceImple): TestInterface
}
```

### 5. ViewModel with Parameters
```kotlin
@HiltViewModel
class MyViewModelWithPara @Inject constructor(
    demoViewModelData: DemoViewModelData,
    @Named("addressOne") address: String
) : ViewModel()
```

### 6. Assisted Injection
```kotlin
@AssistedFactory
interface AssistedViewModelInterface {
    fun create(location: String): AssistedViewModel
}

@HiltViewModel(assistedFactory = AssistedViewModelInterface::class)
class AssistedViewModel @AssistedInject constructor(
    @Assisted private val location: String,
    private val demoViewModelData: DemoViewModelData
) : ViewModel()
```

### 7. API Implementation with Clean Architecture
```kotlin
// API Service Interface
interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
    
    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>
    
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>
    
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}

// Repository Implementation with Error Handling
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUsers(): ApiResult<List<User>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let { users ->
                    ApiResult.Success(users)
                } ?: ApiResult.Error("Empty response body", response.code())
            } else {
                ApiResult.Error("Failed to fetch users: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            val networkException = handleNetworkException(e)
            ApiResult.Error(networkException.message, (networkException as? NetworkException.HttpError)?.code)
        }
    }
}

// ViewModel with StateFlow
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _usersState = MutableStateFlow(UiState<List<User>>())
    val usersState: StateFlow<UiState<List<User>>> = _usersState.asStateFlow()
    
    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = _usersState.value.copy(isLoading = true, error = null)
            
            when (val result = userRepository.getUsers()) {
                is ApiResult.Success -> {
                    _usersState.value = UiState(isLoading = false, data = result.data, error = null)
                }
                is ApiResult.Error -> {
                    _usersState.value = UiState(isLoading = false, data = null, error = result.message, errorCode = result.errorCode)
                }
            }
        }
    }
}

// Network Module for Dependency Injection
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
```

## 🎯 Key Learning Points

1. **Component Scopes**: Understanding different Hilt components (Singleton, Activity, ViewModel)
2. **Qualifiers**: Using `@Named` for multiple implementations of the same type
3. **Module Organization**: Proper module structure and installation
4. **Assisted Injection**: Handling runtime parameters in dependency injection
5. **Compose Integration**: Using Hilt ViewModels in Jetpack Compose
6. **State Management**: Combining StateFlow with Hilt for reactive UI
7. **Clean Architecture**: Proper separation of concerns with data, domain, and UI layers
8. **Network Error Handling**: Comprehensive error handling for different HTTP status codes
9. **Repository Pattern**: Abstracting data sources and business logic
10. **Coroutines Integration**: Using suspend functions with ViewModels and StateFlow
11. **Retrofit Configuration**: Setting up networking with proper interceptors and converters
12. **UI State Management**: Handling loading, success, and error states in Compose

## 🧪 Testing

The project includes basic test structure:
- Unit tests in `src/test/`
- Android instrumented tests in `src/androidTest/`

## 📝 Notes

- The app demonstrates both traditional ViewModel injection and Compose-specific patterns
- Runtime parameter passing is shown using assisted injection
- All examples follow Android and Hilt best practices
- The UI is built with Jetpack Compose for modern Android development
- **NEW**: Complete API implementation with JSONPlaceholder demo API (https://jsonplaceholder.typicode.com/)
- **NEW**: Full CRUD operations with proper error handling and loading states
- **NEW**: Clean Architecture implementation with proper separation of concerns
- **NEW**: Modern UI with Material3 design and intuitive user experience

## 🌐 API Demo Features

The new API Demo activity includes:

### **CRUD Operations**
- **GET**: Fetch all users and individual users by ID
- **POST**: Create new users with form validation
- **PUT**: Update existing users completely
- **PATCH**: Partially update user information
- **DELETE**: Remove users from the system
- **SEARCH**: Find users by name or email with query parameters

### **Error Handling**
- **HTTP Errors**: 400, 401, 403, 404, 422, 429, 500, 502, 503
- **Network Errors**: Timeout, No Internet, Connection issues
- **User-Friendly Messages**: Clear error descriptions for each scenario
- **Error Codes**: Display HTTP status codes for debugging

### **UI Features**
- **Loading States**: Progress indicators during API calls
- **Success States**: Display data with proper formatting
- **Error States**: Show error messages with retry options
- **Form Validation**: Input validation for create/update operations
- **Responsive Design**: Works on different screen sizes
- **Material3 Design**: Modern Android design guidelines

### **Technical Implementation**
- **Clean Architecture**: Data, Domain, UI layers properly separated
- **Repository Pattern**: Abstract data access layer
- **StateFlow**: Reactive state management with Compose
- **Coroutines**: Asynchronous operations with proper error handling
- **Hilt DI**: Complete dependency injection setup
- **Retrofit**: Type-safe HTTP client with Gson serialization

## 🤝 Contributing

Feel free to contribute by:
- Adding more Hilt examples
- Improving documentation
- Fixing bugs or issues
- Adding unit tests

## 📄 License

This project is for educational purposes and demonstrates Hilt dependency injection patterns in Android development.

---

**Author**: Naveen Kumar  
**Package**: com.naveen.hiltexmaple  
**Version**: 1.0
