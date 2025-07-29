package com.example.core.remote.userdetails

import com.example.core.remote.GitHubService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GitHubDetailRepository @Inject constructor(
    private val service: GitHubService,
    private val dispatcher: CoroutineDispatcher
) {
    fun getUserAndRepos(username: String): Flow<UserDetailUiState> = flow {
        emit(UserDetailUiState.Loading)
        try {
            val user = service.getUser(username)
            val repos = service.getRepos(username)
            emit(UserDetailUiState.Success(user, repos))
        } catch (e: Exception) {
            emit(UserDetailUiState.Error("Failed to load details: ${e.message}"))
        }
    }.flowOn(dispatcher)
}