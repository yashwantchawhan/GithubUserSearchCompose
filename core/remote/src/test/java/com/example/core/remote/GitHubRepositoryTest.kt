package com.example.core.remote


import app.cash.turbine.test
import com.example.core.models.userlist.GitHubUserBrief
import com.example.core.remote.userlist.GitHubRepository
import com.example.core.remote.userlist.UserListUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class GitHubRepositoryTest {

    private lateinit var repository: GitHubRepository
    private val service: GitHubService = mockk()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeUsers = listOf(
        GitHubUserBrief(
            login = "octocat",
            avatar_url = "https://github.com/octocat.png"
        )
    )

    @Before
    fun setUp() {
        repository = GitHubRepository(service, testDispatcher)
    }

    @Test
    fun `getUsers emits Loading and then Success when API call is successful`() = runTest {
        coEvery { service.getUsers() } returns fakeUsers

        repository.getUsers().test {
            assertEquals(UserListUiState.Loading, awaitItem())
            val success = awaitItem()
            assertTrue(success is UserListUiState.Success)
            assertEquals(fakeUsers, (success as UserListUiState.Success).users)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUsers emits Loading and then Error when API call throws`() = runTest {
        coEvery { service.getUsers() } throws RuntimeException("Network failure")

        repository.getUsers().test {
            assertEquals(UserListUiState.Loading, awaitItem())
            val error = awaitItem()
            assertTrue(error is UserListUiState.Error)
            assertEquals("Failed to load users: Network failure", (error as UserListUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
