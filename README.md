
## GitHub User Search App
An Android application built with Jetpack Compose, MVVM, and Clean Architecture that allows users to search and view GitHub user profiles and repositories.
The app showcases proper modularization, UI state handling, and clean dependency injection using Hilt.

##  Core Features
- User Listing: Fetch and display a list of GitHub users with avatar and username.
- Search Functionality: Filter users by username.
- User Details: View detailed profile including bio, followers, and non-forked repositories.

## Screenshots

[Screen_recording_20250729_162411.webm](https://github.com/user-attachments/assets/30d8adf5-9909-47ce-a36c-37b2af5595df)

## Technical Features
- Jetpack Compose: Declarative UI framework for building native UI.

- Clean Architecture: Modularized layers (UI, domain, data).

- Hilt: Dependency injection across modules.

- Retrofit: Network layer for API calls.

- Coroutines + Flow: Async operations with lifecycle-aware streams.

- Turbine + Mockk: Reactive unit testing for ViewModels and repositories.

## Directory Structure
```
app/
├── MainActivity.kt                  # Entry point setting up Compose UI
├── GithubUserSearchComposeApp.kt   # App theme & AppNavHost
├── AppNavHost.kt                   # Sets up NavGraph and screens

core/
├── design_system/
│   ├── components/                 # Shared UI components
│   │   ├── AppBar.kt
│   │   ├── AvatarImage.kt
│   │   ├── LoadingIndicator.kt
│   │   ├── SearchBar.kt
│   │   └── UserListCard.kt
│   └── theme/                      # Colors, Typography, Theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt

├── models/
│   ├── userlist/
│   │   └── GitHubUserBrief.kt      # User summary model (username, avatar)
│   └── userdetails/
│       ├── GitHubUser.kt           # Full user info (bio, followers)
│       └── GitHubRepo.kt           # Repository info

├── remote/
│   ├── GitHubService.kt            # Retrofit API service
│   ├── di/
│   │   ├── NetworkModule.kt
│   │   └── DispatcherModule.kt
│   ├── userlist/
│   │   ├── UserListRepository.kt
│   │   └── UserListUiState.kt
│   └── userdetails/
│       ├── UserDetailRepository.kt
│       └── UserDetailUiState.kt

feature/
├── user_list/
│   ├── UserListScreen.kt           # Compose UI with search + list
│   └── UserListViewModel.kt        # Exposes UI state, uses UserListRepository

├── user_details/
│   ├── UserDetailScreen.kt         # Shows full user + repo list
│   └── UserDetailViewModel.kt      # Exposes UI state, uses UserDetailRepository

tests/
├── feature/
│   ├── user_list/
│   │   └── UserListViewModelTest.kt
    |   |__ UserListRepositoryTest
│   └── user_details/
│       ├── UserDetailViewModelTest.kt
│       └── UserDetailRepositoryTest.kt

```
# Testing Strategy
- Unit Tests  ViewModels (Turbine + MockK), repositories
- UI Tests	  Not added but can be done using jetpack compose UI with fake ViewModels
- CI/CD	      GitHub Actions for PR validation and testing

# Setup & Run
- Clone the repo
  - git clone https://github.com/your-username/github-user-search-compose.git
  - cd github-user-search-compose

- Build and run
  - ./gradlew assembleDebug
  - ./gradlew installDebug

# Platform Requirements
Platform	Version
Android	    Min SDK 24
            Target SDK 35

# Design Decisions
- Modular Architecture: Easy to scale and test.
- Clean UI State: Single StateFlow per ViewModel, sealed states.
- Separation of Concerns: UI ↔ ViewModel ↔ Repository ↔ API.
- Testability: High test coverage across ViewModel and repo.
