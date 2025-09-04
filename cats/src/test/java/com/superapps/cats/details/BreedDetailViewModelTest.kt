package com.superapps.cats.details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.superapps.cats.utils.createBreed
import com.superapps.common.ui.components.State
import com.superapps.common.ui.components.UiText
import com.superapps.domain.model.Breed
import com.superapps.domain.usecase.FavouriteUseCase
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
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class BreedDetailViewModelTest {

	private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
	private lateinit var favouriteUseCase: FavouriteUseCase
	private lateinit var viewModel: BreedDetailViewModel

	@Before
	fun setup() {
		Dispatchers.setMain(testDispatcher)
		favouriteUseCase = mock()
		viewModel = BreedDetailViewModel(favouriteUseCase)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun initial_state_is_emptyBreed() = runTest {
		viewModel.state.test {
			val initial = awaitItem()
			assertThat(initial.id).isEmpty()
			assertThat(initial.isFavourite).isFalse()
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun setBreed_updates_state() = runTest {
		val breed = createBreed("1", isFavourite = false)
		viewModel.setBreed(breed)

		viewModel.state.test {
			val state = awaitItem()
			assertThat(state).isEqualTo(breed)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun onFavouriteClick_success_updates_state() = runTest(testDispatcher) {
		val breed = createBreed("1", isFavourite = false)
		viewModel.setBreed(breed)

		val updatedBreed = breed.copy(isFavourite = true)
		whenever(favouriteUseCase(breed.id, true)).thenReturn(flowOf(State.Success(updatedBreed)))

		viewModel.state.test {
			awaitItem() // initial
			viewModel.onFavouriteClick(true)
			advanceUntilIdle()
			val state = awaitItem()
			assertThat(state.isFavourite).isTrue()
			assertThat(state.id).isEqualTo("1")
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun onFavouriteClick_failure_does_not_update_state() = runTest(testDispatcher) {
		val breed = createBreed("1", isFavourite = false)
		viewModel.setBreed(breed)

		val errorState = State.Error<Breed>(UiText.DynamicString("Failed"))
		whenever(favouriteUseCase(breed.id, true)).thenReturn(flowOf(errorState))

		viewModel.state.test {
			awaitItem() // initial
			viewModel.onFavouriteClick(true)

			expectNoEvents()
		}
	}

	@Test
	fun onFavouriteClick_multiple_updates_emit_correct_state() = runTest(testDispatcher) {
		val breed = createBreed("1", isFavourite = false)
		viewModel.setBreed(breed)

		val favBreed = breed.copy(isFavourite = true)
		val unfavBreed = breed.copy(isFavourite = false)

		whenever(favouriteUseCase(breed.id, true)).thenReturn(flowOf(State.Success(favBreed)))
		whenever(favouriteUseCase(breed.id, false)).thenReturn(flowOf(State.Success(unfavBreed)))

		viewModel.state.test {
			awaitItem() // initial

			viewModel.onFavouriteClick(true)
			advanceUntilIdle()
			val afterFav = awaitItem()
			assertThat(afterFav.isFavourite).isTrue()

			viewModel.onFavouriteClick(false)
			advanceUntilIdle()
			val afterUnfav = awaitItem()
			assertThat(afterUnfav.isFavourite).isFalse()

			cancelAndIgnoreRemainingEvents()
		}
	}
}
