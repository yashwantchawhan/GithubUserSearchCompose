package com.example.core.remote.userlist


import com.example.core.remote.GitHubService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class GitHubRepository @Inject constructor(
    private val service: GitHubService,
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher
) {
    fun getUsers(): Flow<UserListUiState> = flow {
        emit(UserListUiState.Loading)
        try {
            val users = service.getUsers()
            emit(UserListUiState.Success(users))
        } catch (e: Exception) {
            emit(UserListUiState.Error("Failed to load users: ${e.message}"))
        }
    }.flowOn(ioDispatcher)
}