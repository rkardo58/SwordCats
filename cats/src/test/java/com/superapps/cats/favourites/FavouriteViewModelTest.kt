package com.superapps.cats.favourites

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.superapps.cats.utils.createBreed
import com.superapps.common.ui.components.State
import com.superapps.common.ui.components.UiText
import com.superapps.domain.model.Breed
import com.superapps.domain.usecase.FavouriteUseCase
import com.superapps.domain.usecase.GetAllFavouriteUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FavouriteViewModelTest {

	private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

	private lateinit var getAllFavouriteUseCase: GetAllFavouriteUseCase
	private lateinit var favouriteUseCase: FavouriteUseCase
	private lateinit var viewModel: FavouriteViewModel

	@Before
	fun setup() {
		Dispatchers.setMain(testDispatcher)
		getAllFavouriteUseCase = mock()
		favouriteUseCase = mock()
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun getFavourites_emits_loading_then_success() = runTest(testDispatcher) {
		val breeds = listOf(createBreed("1"), createBreed("2", lifespan = "12-14"))
		whenever(getAllFavouriteUseCase()).thenReturn(
			flowOf(State.Loading(), State.Success(breeds))
		)

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)

		viewModel.state.test {
			val initial = awaitItem()
			assertThat(initial.breeds).isEmpty()

			val loading = awaitItem()
			assertThat(loading.isLoading).isTrue()

			val success = awaitItem()
			assertThat(success.breeds).hasSize(2)
			assertThat(success.averageLifeSpan).isEqualTo((15 + 14) / 2)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun getFavourites_emits_loading_then_success_when_list_is_empty() = runTest(testDispatcher) {
		val breeds = emptyList<Breed>()
		whenever(getAllFavouriteUseCase()).thenReturn(
			flowOf(State.Loading(), State.Success(breeds))
		)

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)

		viewModel.state.test {
			val initial = awaitItem()
			assertThat(initial.breeds).isEmpty()

			val loading = awaitItem()
			assertThat(loading.isLoading).isTrue()

			val success = awaitItem()
			assertThat(success.breeds).hasSize(0)
			assertThat(success.averageLifeSpan).isEqualTo(0)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun getFavourites_emits_error() = runTest(testDispatcher) {
		val message = UiText.DynamicString("Boom")
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Error(message)))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)

		viewModel.state.test {
			awaitItem()
			val error = awaitItem()
			assertThat(error.isLoading).isFalse()
			assertThat(error.breeds).isEmpty()
			assertThat(error.error).isEqualTo(message)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun removeFromFav_updates_state_after_success() = runTest(testDispatcher) {
		val breeds = listOf(createBreed("1", isFavourite = true), createBreed("2"))
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Success(breeds)))
		whenever(favouriteUseCase(any(), any())).thenReturn(flowOf(State.Success(createBreed("1", isFavourite = false))))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)

		viewModel.state.test {
			awaitItem()
			val state = awaitItem()
			assertThat(state.breeds.first { it.id == "1" }.isFavourite).isTrue()
			assertThat(state.breeds).hasSize(breeds.size)

			viewModel.removeFromFav("1")
			advanceUntilIdle()
			val result = awaitItem()

			assertThat(result.breeds.any { it.id == "1" }).isFalse()
			assertThat(result.breeds).hasSize(breeds.size - 1)
			cancelAndIgnoreRemainingEvents()
		}
	}
}
