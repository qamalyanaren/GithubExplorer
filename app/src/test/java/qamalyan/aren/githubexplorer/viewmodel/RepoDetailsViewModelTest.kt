package qamalyan.aren.githubexplorer.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.usecase.GithubRepoOneUseCase
import qamalyan.aren.githubexplorer.ui.details.RepoDetailsViewModel

@RunWith(JUnit4::class)
class RepoDetailsViewModelTest {
    @Mock
    lateinit var githubRepoOneUseCase: GithubRepoOneUseCase

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // setting up test dispatcher as main dispatcher for coroutines
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `Given the ViewModel is initialized, then get repo from database is success`() {
        runTest {
            Mockito.`when`(githubRepoOneUseCase(1000L))
                .thenReturn(flowOf(RepoDetailsUiEntity.DEFAULT))

            val savedStateHandle = SavedStateHandle(
                mapOf("args" to 1000L)
            )
            val sut = RepoDetailsViewModel(
                githubRepoOneUseCase,
                savedStateHandle
            )

            assertTrue(sut.repoDetailsEntity.value == RepoDetailsUiEntity.DEFAULT)
        }
    }

    @Test(expected = RuntimeException::class)
    fun `Given the ViewModel is initialized, then get repo from database is failed`() {
        runTest {
            Mockito.`when`(githubRepoOneUseCase(-1L))
                .thenThrow(RuntimeException("Repo not found"))

            val savedStateHandle = SavedStateHandle(
                mapOf("args" to -1L)
            )
            val sut = RepoDetailsViewModel(
                githubRepoOneUseCase,
                savedStateHandle
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // removing the test dispatcher
        Dispatchers.resetMain()
    }
}