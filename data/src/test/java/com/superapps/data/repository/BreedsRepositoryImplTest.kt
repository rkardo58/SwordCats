package com.superapps.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.superapps.common.Resource
import com.superapps.common.ui.components.State
import com.superapps.data.data.createBreedDto
import com.superapps.data.data.createBreedEntity
import com.superapps.data.database.dao.BreedsDao
import com.superapps.data.database.model.BreedEntity
import com.superapps.data.network.CatsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
@RunWith(JUnit4::class)
class BreedsRepositoryImplTest {

	private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

	private lateinit var api: CatsApi
	private lateinit var breedDao: BreedsDao
	private lateinit var mediator: BreedsRemoteMediator
	private lateinit var repository: BreedsRepositoryImpl

	@Before
	fun setup() {
		Dispatchers.setMain(testDispatcher)
		api = mock()
		breedDao = mock()
		mediator = mock()

		repository = BreedsRepositoryImpl(api, breedDao, mediator)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun searchBreeds_returns_success_from_remote() = runTest {
		val remote = listOf(createBreedDto("1"), createBreedDto("2"))
		whenever(api.searchBreeds("query")).thenReturn(remote)
		whenever(breedDao.getAllFavourite()).thenReturn(flowOf(emptyList()))
		whenever(breedDao.insertAll(any())).thenReturn(Unit)

		val result = repository.searchBreeds("query")
		assertThat(result).isInstanceOf(Resource.Success::class.java)
		assertThat((result as Resource.Success).data.map { it.id }).containsExactly("1", "2")
	}

	@Test
	fun searchBreeds_returns_cached_if_remote_fails() = runTest {
		val name = "Persian"
		whenever(api.searchBreeds(name.substring(0, 2))).thenThrow(RuntimeException("Boom"))
		val cached = listOf(createBreedEntity("1", name), createBreedEntity("2", "Siamese"))
		whenever(breedDao.getAll()).thenReturn(cached)
		whenever(breedDao.getAllFavourite()).thenReturn(flowOf())

		val result = repository.searchBreeds("per")
		assertThat(result).isInstanceOf(Resource.Success::class.java)
		assertThat((result as Resource.Success).data.first().name).isEqualTo(name)
	}

	@Test
	fun getAllFavourite_emits_success() = runTest {
		val entities = listOf(createBreedEntity("1", isFavourite = true))
		whenever(breedDao.getAllFavourite()).thenReturn(flowOf(entities))

		repository.getAllFavourite().test {
			val state = awaitItem()
			assertThat(state).isInstanceOf(State.Success::class.java)
			assertThat((state as State.Success).data.first().id).isEqualTo("1")
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun getAllFavourite_emits_error_if_exception() = runTest {
		whenever(breedDao.getAllFavourite()).thenAnswer { throw RuntimeException("Boom") }

		repository.getAllFavourite().test {
			val state = awaitItem()
			assertThat(state).isInstanceOf(State.Error::class.java)
			cancelAndIgnoreRemainingEvents()
		}
	}

	@Test
	fun toggleFavourite_updates_existing_breed() = runTest {
		val breedEntity = createBreedEntity("1", isFavourite = false)
		whenever(breedDao.getById("1")).thenReturn(breedEntity)
		whenever(breedDao.insert(any())).thenReturn(Unit)

		val result = repository.toggleFavourite("1", true)

		assertThat(result).isInstanceOf(Resource.Success::class.java)
		assertThat((result as Resource.Success).data.isFavourite).isTrue()
	}

	@Test
	fun toggleFavourite_returns_failed_if_not_found() = runTest {
		whenever(breedDao.getById("1")).thenReturn(null)

		val result = repository.toggleFavourite("1", true)

		assertThat(result).isInstanceOf(Resource.Failed::class.java)
	}
}

class FakePagingSource(private val items: List<BreedEntity>) : PagingSource<Int, BreedEntity>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BreedEntity> =
		LoadResult.Page(data = items, prevKey = null, nextKey = null)
	override fun getRefreshKey(state: PagingState<Int, BreedEntity>): Int? = null
}
