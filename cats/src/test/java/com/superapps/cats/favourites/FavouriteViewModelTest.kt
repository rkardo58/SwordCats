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
import org.mockito.Mockito.verify
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
	fun `observeFavourites emits loading then success`() = runTest {
		val breeds = listOf(createBreed("1", lifespan = "10-14"), createBreed("2", lifespan = "12-16"))
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Loading(), State.Success(breeds)))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)

		viewModel.state.test {
			val initial = awaitItem()
			assertThat(initial.isLoading).isFalse()

			val loading = awaitItem()
			assertThat(loading.isLoading).isTrue()

			val success = awaitItem()
			assertThat(success.isLoading).isFalse()
			assertThat(success.breeds).hasSize(2)
			// Average lifespan = (14+16)/2 = 15
			assertThat(success.averageLifeSpan).isEqualTo(15)

			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun `observeFavourites emits error`() = runTest {
		val errorMessage = UiText.DynamicString("Boom")
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Error(errorMessage)))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)
		advanceUntilIdle()

		viewModel.state.test {
			val errorState = awaitItem()
			assertThat(errorState.isLoading).isFalse()
			assertThat(errorState.error).isEqualTo(errorMessage)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun `observeFavourites handles empty list`() = runTest {
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Success(emptyList())))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)
		advanceUntilIdle()

		viewModel.state.test {
			val state = awaitItem()
			assertThat(state.breeds).isEmpty()
			assertThat(state.averageLifeSpan).isEqualTo(0)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun `getLifeSpan calculates average correctly`() = runTest {
		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)
		val breeds = listOf(
			createBreed("1", lifespan = "10-12"),
			createBreed("2", lifespan = "14-16"),
			createBreed("3", lifespan = "invalid") // should be ignored
		)
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Success(breeds)))
		val average = viewModel.run {
			// Using reflection since getLifeSpan is private
			val method = FavouriteViewModel::class.java.getDeclaredMethod("getLifeSpan", List::class.java)
			method.isAccessible = true
			method.invoke(this, breeds) as Int
		}
		assertThat(average).isEqualTo((12 + 16) / 2) // 14
	}

	@Test
	fun `removeFromFav calls favouriteUseCase and emits error if failed`() = runTest {
		val id = "123"
		val errorState = State.Error<Breed>(UiText.DynamicString("Fail"))

		val breeds = listOf(createBreed("1", lifespan = "10-14"), createBreed("2", lifespan = "12-16"))
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Success(breeds)))

		whenever(favouriteUseCase(id, false)).thenReturn(flowOf(errorState))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)
		advanceUntilIdle()

		viewModel.removeFromFav(id)
		advanceUntilIdle()

		verify(favouriteUseCase).invoke(id, false)

		viewModel.state.test {
			val state = awaitItem()
			assertThat(state.error).isEqualTo(UiText.DynamicString("Fail"))
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun `removeFromFav success does not emit error`() = runTest {
		val id = "1"
		val breed = createBreed(id, lifespan = "10-14")
		val breeds = listOf(breed, createBreed("2", lifespan = "12-16"))
		whenever(getAllFavouriteUseCase()).thenReturn(flowOf(State.Loading(), State.Success(breeds)))
		whenever(favouriteUseCase(id, false)).thenReturn(flowOf(State.Success(breed)))

		viewModel = FavouriteViewModel(getAllFavouriteUseCase, favouriteUseCase)
		advanceUntilIdle()

		viewModel.removeFromFav(id)
		advanceUntilIdle()

		verify(favouriteUseCase).invoke(id, false)

		viewModel.state.test {
			val state = awaitItem()
			assertThat(state.error).isNull()
			cancelAndIgnoreRemainingEvents()
		}
	}
}
