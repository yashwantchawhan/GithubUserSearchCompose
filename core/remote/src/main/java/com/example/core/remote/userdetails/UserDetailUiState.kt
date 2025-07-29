package com.example.core.remote.userdetails

import com.example.core.models.userdetails.GitHubRepo
import com.example.core.models.userdetails.GitHubUser

sealed class UserDetailUiState {
    data object Loading : UserDetailUiState()
    data class Success(
        val user: GitHubUser,
        val repos: List<GitHubRepo>
    ) : UserDetailUiState()

    data class Error(val message: String) : UserDetailUiState()
}