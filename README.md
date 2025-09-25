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

## 🎯 Key Learning Points

1. **Component Scopes**: Understanding different Hilt components (Singleton, Activity, ViewModel)
2. **Qualifiers**: Using `@Named` for multiple implementations of the same type
3. **Module Organization**: Proper module structure and installation
4. **Assisted Injection**: Handling runtime parameters in dependency injection
5. **Compose Integration**: Using Hilt ViewModels in Jetpack Compose
6. **State Management**: Combining StateFlow with Hilt for reactive UI

## 🧪 Testing

The project includes basic test structure:
- Unit tests in `src/test/`
- Android instrumented tests in `src/androidTest/`

## 📝 Notes

- The app demonstrates both traditional ViewModel injection and Compose-specific patterns
- Runtime parameter passing is shown using assisted injection
- All examples follow Android and Hilt best practices
- The UI is built with Jetpack Compose for modern Android development

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
