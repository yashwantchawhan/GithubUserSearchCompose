@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.feature.user_details

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.esign_system.components.ErrorMessage
import com.example.core.esign_system.components.LoadingIndicator
import com.example.core.models.userdetails.GitHubRepo
import com.example.core.models.userdetails.GitHubUser
import com.example.core.remote.userdetails.UserDetailUiState


@Composable
fun UserDetailScreen(username: String, onBack: () -> Unit) {
    val viewModel: UserDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(username) {
        viewModel.loadDetails(username)
    }

    when (val state = uiState) {
        is UserDetailUiState.Loading -> LoadingIndicator()
        is UserDetailUiState.Error -> ErrorMessage(state.message)
        is UserDetailUiState.Success -> {
            val user = state.user
            val repos = state.repos
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(user.login) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                DetailContent(
                    user = user,
                    repos = repos,
                    onRepoClick = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DetailContent(
    user: GitHubUser,
    repos: List<GitHubRepo>,
    onRepoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = user.avatar_url,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text("${user.followers} Followers · ${user.following} Following")
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        Text("Repositories", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(repos) { repo ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRepoClick(repo.html_url) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(repo.name, style = MaterialTheme.typography.titleSmall)
                            Text("★ ${repo.stargazers_count}")
                        }

                        if (!repo.language.isNullOrBlank()) {
                            Text(repo.language!!, style = MaterialTheme.typography.labelSmall)
                        }

                        Spacer(Modifier.height(4.dp))

                        Text(repo.description ?: "", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}