
package com.example.feature.user_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.esign_system.components.AppBar
import com.example.core.esign_system.components.ErrorMessage
import com.example.core.esign_system.components.LoadingIndicator
import com.example.core.esign_system.components.SearchBar
import com.example.core.esign_system.components.UserListCard
import com.example.core.remote.userlist.UserListUiState


@Composable
fun UserListScreen(
    onUserClick: (String) -> Unit
) {
    val viewModel: GitHubUserListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppBar(title = "GitHub Users") }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding(),
                        start = 16.dp,
                        end = 16.dp
                    )
                )
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = "Search users..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is UserListUiState.Loading -> LoadingIndicator()

                is UserListUiState.Error -> ErrorMessage(state.message)

                is UserListUiState.Success -> {
                    val filtered = state.users.filter {
                        it.login.contains(query, ignoreCase = true)
                    }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filtered) { user ->
                            UserListCard(
                                username = user.login,
                                avatarUrl = user.avatar_url,
                                onClick = { onUserClick(user.login) }
                            )
                        }
                    }
                }
            }
        }
    }
}
