package com.example.feature.user_list


import app.cash.turbine.test
import com.example.core.models.userlist.GitHubUserBrief
import com.example.core.remote.userlist.UserListRepository
import com.example.core.remote.userlist.UserListUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserListViewModelTest {

    private lateinit var viewModel: UserListViewModel
    private val repository: UserListRepository = mockk()

    private val fakeUsers = listOf(
        GitHubUserBrief(login = "octocat", avatar_url = "https://github.com/octocat.png")
    )

    @Before
    fun setUp() {
        coEvery { repository.getUsers() } returns flow {
            emit(UserListUiState.Success(fakeUsers))
        }
        viewModel = UserListViewModel(repository)
    }

    @Test
    fun `getUsers - when repository emits Success - emits Success UI state`() = runTest {
        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is UserListUiState.Success)
            assert((item as UserListUiState.Success).users == fakeUsers)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUsers - when repository emits Error - emits Error UI state`() = runTest {
        coEvery { repository.getUsers() } returns flow {
            emit(UserListUiState.Error("Something went wrong"))
        }

        viewModel = UserListViewModel(repository)

        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is UserListUiState.Error)
            assert((item as UserListUiState.Error).message == "Something went wrong")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUsers - when repository emits Loading - emits Loading UI state`() = runTest {
        coEvery { repository.getUsers() } returns flow {
            emit(UserListUiState.Loading)
        }

        viewModel = UserListViewModel(repository)

        viewModel.uiState.test {
            val item = awaitItem()
            assert(item is UserListUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
