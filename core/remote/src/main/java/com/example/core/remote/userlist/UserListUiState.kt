package com.example.core.remote.userlist

import com.example.core.models.userlist.GitHubUserBrief

sealed class UserListUiState {
    data object Loading : UserListUiState()
    data class Success(val users: List<GitHubUserBrief>) : UserListUiState()
    data class Error(val message: String) : UserListUiState()
}
