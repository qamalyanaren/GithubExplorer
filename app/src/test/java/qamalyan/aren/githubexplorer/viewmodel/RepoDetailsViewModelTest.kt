package qamalyan.aren.githubexplorer.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import qamalyan.aren.domain.InfoEvent
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.usecase.GithubRepoOneUseCase
import qamalyan.aren.domain.utils.TextSource
import qamalyan.aren.githubexplorer.ui.details.RepoDetailsViewModel
import qamalyan.aren.githubexplorer.util.MainCoroutineScopeRule

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RepoDetailsViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var githubRepoOneUseCase: GithubRepoOneUseCase


    private lateinit var viewModel: RepoDetailsViewModel


    @Test
    fun `Flows update when the UseCase emits data`() {
        runTest {
            // Arrange
            val testData = RepoDetailsUiEntity(
                id = 1000L,
                ownerAvatarUrl = null,
                ownerName = "Qamalyan",
                name = "repo",
                description = "",
                starsCount = 1,
                forksCount = 2,
                language = "Kotlin",
                webUrl = ""

            )
            val testDataFlow = flowOf(testData)

            Mockito.`when`(githubRepoOneUseCase(1000L))
                .thenReturn(testDataFlow)

            val savedStateHandle = SavedStateHandle(mapOf("args" to 1000L))
            viewModel = RepoDetailsViewModel(
                githubRepoOneUseCase,
                savedStateHandle
            )
            // Act and Assert
            viewModel.repoDetailsEntity.test {
                val firstItem = awaitItem()
                assertEquals(firstItem.id, RepoDetailsUiEntity.DEFAULT.id)
                val secondItem = awaitItem()
                assertEquals(secondItem, testData)
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `ViewModel handles errors from the UseCase`() {
        runTest {

            // Arrange
            val repoNotFountError = RuntimeException("Repo not found")
            val errorFlow = flow<RepoDetailsUiEntity> { throw repoNotFountError }

            Mockito.`when`(githubRepoOneUseCase(-1L)).thenReturn(errorFlow)

            val savedStateHandle = SavedStateHandle(mapOf("args" to -1L))
            viewModel = RepoDetailsViewModel(githubRepoOneUseCase, savedStateHandle)

            // Act and Assert
            turbineScope {
                val repoDetailsTurbine = viewModel.repoDetailsEntity.testIn(backgroundScope)
                val infoEventTurbine = viewModel.infoEvent.testIn(backgroundScope)

                assertEquals(repoDetailsTurbine.awaitItem(), RepoDetailsUiEntity.DEFAULT)

                val eventItem = (infoEventTurbine.awaitItem() as? InfoEvent.ErrorAlert)
                val textSourceMessage = eventItem?.message as? TextSource.Dynamic
                assertTrue(eventItem is InfoEvent.ErrorAlert)
                assertTrue(textSourceMessage is TextSource.Dynamic)
                assertEquals(textSourceMessage?.value, repoNotFountError.message)
            }
        }
    }
}