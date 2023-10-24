package qamalyan.aren.githubexplorer.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.domain.usecase.GithubRepoSearchUseCase
import qamalyan.aren.githubexplorer.ui.main.RepoSearchViewModel
import qamalyan.aren.githubexplorer.ui.main.UiAction
import qamalyan.aren.githubexplorer.ui.main.UiState

@RunWith(JUnit4::class)
class SearchRepoViewModelTest {
    @Mock
    lateinit var githubRepoSearchUseCase: GithubRepoSearchUseCase

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // setting up test dispatcher as main dispatcher for coroutines
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `Given the sut is initialized, then it waits for event`() {

        val sut = RepoSearchViewModel(
            githubRepoSearchUseCase,
            savedStateHandle
        )
        assertTrue(sut.state.value == UiState())
    }

    @Test
    fun `Given the ViewModel waits - When the event Search comes, then execute search with result`() =

        runTest {
            Mockito.`when`(githubRepoSearchUseCase("Android"))
                .thenReturn(
                    flowOf(PagingData.empty<RepoUiEntity>())
                )
            val sut = RepoSearchViewModel(githubRepoSearchUseCase, savedStateHandle)

            val uiState = UiState(
                query = "Android",
                lastQueryScrolled = "Android",
                hasNotScrolledForCurrentSearch = false
            )
            sut.state.test {
                sut.acceptAction(UiAction.Search("Android"))

                assertEquals(uiState, awaitItem())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // removing the test dispatcher
        Dispatchers.resetMain()
    }
}