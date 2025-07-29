package com.example.feature.user_details


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.remote.userdetails.GitHubDetailRepository
import com.example.core.remote.userdetails.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: GitHubDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    fun loadDetails(username: String) {
        viewModelScope.launch {
            repository.getUserAndRepos(username).collect {
                _uiState.value = it
            }
        }
    }
}
