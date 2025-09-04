package com.superapps.cats.breeds

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.superapps.cats.utils.createBreed
import com.superapps.common.ui.components.State
import com.superapps.common.ui.components.UiText
import com.superapps.domain.usecase.FavouriteUseCase
import com.superapps.domain.usecase.GetBreedsPaged
import com.superapps.domain.usecase.SearchBreed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class BreedsViewModelTest {

	private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

	private lateinit var getBreedsPaged: GetBreedsPaged
	private lateinit var favouriteUseCase: FavouriteUseCase
	private lateinit var searchBreed: SearchBreed
	private lateinit var viewModel: BreedsViewModel

	@Before
	fun setup() {
		Dispatchers.setMain(testDispatcher)
		getBreedsPaged = mock()
		favouriteUseCase = mock()
		searchBreed = mock()

		val pagingFlow = flowOf(PagingData.from(listOf(createBreed("1"), createBreed("2"))))
		whenever(getBreedsPaged()).thenReturn(pagingFlow)

		viewModel = BreedsViewModel(getBreedsPaged, favouriteUseCase, searchBreed)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun onSearchQueryChange_with_empty_query_resets_breeds() = runTest {
		advanceUntilIdle()
		viewModel.state.test {
			awaitItem()

			viewModel.onSearchQueryChange("")

			expectNoEvents()
		}
	}

	@Test
	fun onSearchQueryChange_with_non_empty_query_emits_success() = runTest {
		val searchedBreed = listOf(createBreed("3"), createBreed("2"))
		whenever(searchBreed("abc")).thenReturn(flowOf(State.loading(), State.Success(searchedBreed)))
		advanceUntilIdle()
		viewModel.state.test {
			awaitItem()

			viewModel.onSearchQueryChange("abc", 0)
			val queryChanged = awaitItem()
			assertThat(queryChanged.searchQuery).isEqualTo("abc")

			val loadingItem = awaitItem()
			assertThat(loadingItem.isLoading).isTrue()

			val breeds = awaitItem().breeds.asSnapshot()
			assertThat(breeds).hasSize(2)
			assertThat(breeds.first().id).isEqualTo("3")
		}
	}

	@Test
	fun onSearchQueryChange_emits_error() = runTest {
		val errorString = UiText.DynamicString("Boom")
		whenever(searchBreed("abc")).thenReturn(flowOf(State.Error(errorString)))
		viewModel.state.test {
			awaitItem()

			viewModel.onSearchQueryChange("abc")
			skipItems(2)

			val error = viewModel.state.value.error
			assertThat(error).isEqualTo(errorString)
		}
	}

	@Test
	fun onSearchQueryChange_with_empty_result_emits_success_with_empty_list() = runTest {
		whenever(searchBreed("xyz")).thenReturn(flowOf(State.Success(emptyList())))
		viewModel.onSearchQueryChange("xyz")
		advanceUntilIdle()

		val breeds = viewModel.state.value.breeds.asSnapshot()
		assertThat(breeds).isEmpty()
	}

	@Test
	fun onToggleFavorite_updates_breed_when_searchQuery_is_not_empty() = runTest {
		val breed = createBreed("1", isFavourite = false)
		advanceUntilIdle()
		viewModel.state.test {
			awaitItem()
			viewModel.onSearchQueryChange("abc")
			whenever(searchBreed("abc")).thenReturn(flowOf(State.Success(listOf(breed))))

			val queryChanged = awaitItem()
			assertThat(queryChanged.searchQuery).isEqualTo("abc")

			val queryResult = awaitItem()
			assertThat(queryResult.breeds.asSnapshot().first().isFavourite).isFalse()

			whenever(favouriteUseCase("1", true)).thenReturn(flowOf(State.Success(breed.copy(isFavourite = true))))

			viewModel.onToggleFavorite("1", true)

			val updated = awaitItem().breeds.asSnapshot().first()
			assertThat(updated.isFavourite).isTrue()
		}
	}

	@Test
	fun onToggleFavorite_handles_error() = runTest {
		val breed = createBreed("1", isFavourite = false)
		advanceUntilIdle()

		viewModel.state.test {
			awaitItem()
			viewModel.onSearchQueryChange("abc")
			whenever(searchBreed("abc")).thenReturn(flowOf(State.Success(listOf(breed))))

			val queryChanged = awaitItem()
			assertThat(queryChanged.searchQuery).isEqualTo("abc")

			val queryResult = awaitItem()
			assertThat(queryResult.breeds.asSnapshot().first().isFavourite).isFalse()

			val error = UiText.DynamicString("Boom")
			whenever(favouriteUseCase("1", true)).thenReturn(flowOf(State.Error(error)))

			viewModel.onToggleFavorite("1", true)

			expectNoEvents()
		}
	}
}
