# Sword Cats App

An Android application built with Kotlin and Jetpack Compose

## Features

1. Single Activity with single NavHost
	1.1 - Uses compose navigation, both by route and by SafeArgs Classes
2. Room Database for persistence 
    2.1 - In combination with paging to provide a better experience for the user
3. Favourites are stored in the database locally and can be viewed in the favourites screen
	3.1 - All the screens can update the favourites state
4. Search functionality through API
5. API Key is stored in local.properties file
6. Breeds screen gets information through a mediator to handle paging and API/Local data
7. Details screen gets information has a safeArg from navigation.
8. Unit Tests were done to a small set of classes just to show some structure without being too intensive.
9. Some integration tests were also done.

## Technical Decisions / Architecture

- **MVVM Architecture**: Separates UI from business logic, making testing easier, even on these cases I would prefer more of an MVI approach.
- **Mediator + Room + API**: Ensures offline-first behaviour while supporting paging from network.
- **Single Activity / Single NavHost**: Simplifies navigation and state handling.
- **SafeArgs**: Provides type-safe navigation between screens.
- **Modules**: Improves maintainability, testability, and separation of concerns.

## Libraries & Tools

- Kotlin, Jetpack Compose 
- Hilt (Dependency Injection)
- Retrofit + OkHttp (Networking) 
- Room (Local Database)
- Coil (Image Loading)
- Coroutines + Flow (Asynchronous programming)
- JUnit + Mockito + Turbine + Truth (Testing)
- Jetpack Navigation Component
- Timber (Logging)
- Ktlint (Code style)
- Paging 3 (Pagination)
- Kotlin Serialization (JSON)

## Setup & Running

1. Clone the repository:
   ```bash
   git clone https://github.com/rkardo58/SwordCats.git

2. Open the project in Android Studio.
3. Add your Cat API key in local.properties:
   **CAT_API_KEY = your_api_key_here**
4. Build and run the app on an emulator or real device.
