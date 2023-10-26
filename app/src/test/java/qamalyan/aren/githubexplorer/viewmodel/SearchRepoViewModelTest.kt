package qamalyan.aren.githubexplorer.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import qamalyan.aren.domain.usecase.GithubRepoSearchUseCase
import qamalyan.aren.githubexplorer.common.navigation.Command
import qamalyan.aren.githubexplorer.ui.main.RepoSearchViewModel
import qamalyan.aren.githubexplorer.ui.main.SearchRepoUiState
import qamalyan.aren.githubexplorer.util.MainCoroutineScopeRule


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class SearchRepoViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var githubRepoSearchUseCase: GithubRepoSearchUseCase

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: RepoSearchViewModel

    @Before
    fun setUpViewModel() {
        viewModel = RepoSearchViewModel(githubRepoSearchUseCase, savedStateHandle)
    }

    @Test
    fun `test accepting new search query should update state`() = runTest {
        // Arrange
        val defaultSearchRepoUiState = SearchRepoUiState(
            query = "Android",
            lastQueryScrolled = "Android",
            hasNotScrolledForCurrentSearch = false
        )

        val afterQuerySearchRepoUiState = SearchRepoUiState(
            query = "Kotlin",
            lastQueryScrolled = "Android",
            hasNotScrolledForCurrentSearch = true
        )

        // Act
        viewModel.setQueryValue("Kotlin")

        // Assert
        viewModel.state.test {
            val firstItem = awaitItem()
            assertEquals(firstItem, defaultSearchRepoUiState)
            val secondItem = awaitItem()
            assertEquals(secondItem, afterQuerySearchRepoUiState)
        }
    }

    @Test
    fun `test accepting new search query in scrolling should update state`() = runTest {
        // Arrange
        val defaultSearchRepoUiState = SearchRepoUiState(
            query = "Android",
            lastQueryScrolled = "Android",
            hasNotScrolledForCurrentSearch = false
        )

        val afterScrollingSearchRepoUiState = SearchRepoUiState(
            query = "Android",
            lastQueryScrolled = "Kotlin",
            hasNotScrolledForCurrentSearch = true
        )

        // Act
        viewModel.setScrollingQueryValue("Kotlin")

        // Assert
        viewModel.state.test {
            val firstItem = awaitItem()
            assertEquals(firstItem, defaultSearchRepoUiState)
            val secondItem = awaitItem()
            assertEquals(secondItem, afterScrollingSearchRepoUiState)
        }
    }

    @Test
    fun `test navigation command should emit NavCommand`() = runTest {
        // Act
        viewModel.navigateToRepoDetails(123L)

        // Assert
        viewModel.command.test {
            val command = awaitItem()
            assertTrue(command is Command.NavCommand)
        }
    }
}