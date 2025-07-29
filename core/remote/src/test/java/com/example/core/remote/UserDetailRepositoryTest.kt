package com.example.core.remote


import app.cash.turbine.test
import com.example.core.models.userdetails.GitHubRepo
import com.example.core.models.userdetails.GitHubUser
import com.example.core.remote.userdetails.UserDetailRepository
import com.example.core.remote.userdetails.UserDetailUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class UserDetailRepositoryTest {

    private lateinit var repository: UserDetailRepository
    private val service: GitHubService = mockk()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeUser = GitHubUser(
        login = "octocat",
        followers = 100,
        following = 50,
        avatar_url = "https://github.com/octocat.png"
    )

    private val fakeRepos = listOf(
        GitHubRepo(
            name = "demo-repo",
            description = "A sample repo",
            language = "Kotlin",
            stargazers_count = 42,
            html_url = "https://github.com/octocat/demo-repo"
        )
    )

    @Before
    fun setup() {
        repository = UserDetailRepository(service, testDispatcher)
    }

    @Test
    fun `getUserAndRepos emits Loading and then Success`() = runTest {
        coEvery { service.getUser("octocat") } returns fakeUser
        coEvery { service.getRepos("octocat") } returns fakeRepos

        repository.getUserAndRepos("octocat").test {
            assertEquals(UserDetailUiState.Loading, awaitItem())
            val success = awaitItem()
            assertTrue(success is UserDetailUiState.Success)
            assertEquals(fakeUser, (success as UserDetailUiState.Success).user)
            assertEquals(fakeRepos, success.repos)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUserAndRepos emits Loading and then Error on failure`() = runTest {
        coEvery { service.getUser("octocat") } throws RuntimeException("API down")
        coEvery { service.getRepos("octocat") } returns emptyList()

        repository.getUserAndRepos("octocat").test {
            assertEquals(UserDetailUiState.Loading, awaitItem())
            val error = awaitItem()
            assertTrue(error is UserDetailUiState.Error)
            assertEquals("Failed to load details: API down", (error as UserDetailUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
