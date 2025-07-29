package com.example.feature.user_details

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue


import app.cash.turbine.test
import com.example.core.models.userdetails.GitHubRepo
import com.example.core.models.userdetails.GitHubUser
import com.example.core.remote.userdetails.UserDetailRepository
import com.example.core.remote.userdetails.UserDetailUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class UserDetailViewModelTest {

    private lateinit var viewModel: UserDetailViewModel
    private val repository: UserDetailRepository = mockk()

    private val fakeUser = GitHubUser(
        login = "octocat",
        followers = 100,
        following = 50,
        avatar_url = "https://github.com/octocat.png"
    )

    private val fakeRepos = listOf(
        GitHubRepo(
            name = "demo-repo",
            description = "Demo repository",
            language = "Kotlin",
            stargazers_count = 42,
            html_url = "https://github.com/octocat/demo-repo"
        )
    )

    @Test
    fun `loadDetails emits Success when repository returns user and repos`() = runTest {
        coEvery { repository.getUserAndRepos("octocat") } returns flow {
            emit(UserDetailUiState.Success(fakeUser, fakeRepos))
        }

        viewModel = UserDetailViewModel(repository)

        viewModel.uiState.test {
            // First item will always be the default Loading from MutableStateFlow
            assertTrue(awaitItem() is UserDetailUiState.Loading)

            // Now we call the actual method to load data
            viewModel.loadDetails("octocat")

            // Then Success should be emitted from the flow
            val item = awaitItem()
            assertTrue(item is UserDetailUiState.Success)
            assertEquals(fakeUser, (item as UserDetailUiState.Success).user)
            assertEquals(fakeRepos, item.repos)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `loadDetails emits Error when repository throws`() = runTest {
        coEvery { repository.getUserAndRepos("failuser") } returns flow {
            emit(UserDetailUiState.Error("User not found"))
        }

        viewModel = UserDetailViewModel(repository)
        viewModel.loadDetails("failuser")

        viewModel.uiState.test {
            assertEquals(UserDetailUiState.Loading, awaitItem()) // Initial
            val error = awaitItem()
            assertTrue(error is UserDetailUiState.Error)
            assertEquals("User not found", (error as UserDetailUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
