package com.superapps.data.repository

import androidx.paging.ExperimentalPagingApi
import com.google.common.truth.Truth.assertThat
import com.superapps.common.Resource
import com.superapps.data.data.createBreedDto
import com.superapps.data.data.createBreedEntity
import com.superapps.data.database.SwordCatsDataBase
import com.superapps.data.database.dao.BreedsDao
import com.superapps.data.network.CatsApi
import com.superapps.domain.repository.BreedsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
@RunWith(JUnit4::class)
class BreedsRepositoryImplTest {

	private lateinit var repository: BreedsRepository
	private lateinit var api: CatsApi
	private lateinit var dao: BreedsDao
	private lateinit var db: SwordCatsDataBase

	private val dispatcher = StandardTestDispatcher()

	@Before
	fun setup() {
		Dispatchers.setMain(dispatcher)
		api = mock()
		dao = mock()
		db = mock()
		whenever(db.breedsDao()).thenReturn(dao)

		repository = BreedsRepositoryImpl(api, db)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}


		@Test
		fun searchBreeds_returns_success_from_remote() = runTest {
			val dto = createBreedDto("1", "Siamese")
			whenever(api.searchBreeds("siamese")).thenReturn(listOf(dto))
			whenever(dao.getAllFavourite()).thenReturn(emptyList())

			val result = repository.searchBreeds("siamese")

			assert(result is Resource.Success)
			val data = (result as Resource.Success).data
			assertThat(data.first().id).isEqualTo("1")
			verify(dao).insertAll(any())
		}

		@Test
		fun searchBreeds_falls_back_to_cache_on_error() = runTest {
			whenever(api.searchBreeds("siamese")).thenThrow(RuntimeException("Network error"))
			whenever(dao.getAll()).thenReturn(listOf(createBreedEntity("1", "Siamese")))

			val result = repository.searchBreeds("siamese")

			assert(result is Resource.Success)
			val data = (result as Resource.Success).data
			assertThat(data.first().name).isEqualTo("Siamese")
		}

	@Test
	fun getAllFavourite_returns_favourites() = runTest {
		whenever(dao.getAllFavourite()).thenReturn(listOf(createBreedEntity("1", "Siamese", true)))

		val result = repository.getAllFavourite()

		assert(result is Resource.Success)
		assertThat((result as Resource.Success).data.first().isFavourite).isTrue()
	}

	@Test
	fun getAllFavourite_returns_failed_on_exception() = runTest {
		whenever(dao.getAllFavourite()).thenThrow(RuntimeException("DB fail"))

		val result = repository.getAllFavourite()

		assert(result is Resource.Failed)
	}

	@Test
	fun toggleFavourite_updates_existing_breed() = runTest {
		val entity = createBreedEntity("1", "Siamese", false)
		whenever(dao.getById("1")).thenReturn(entity)

		val result = repository.toggleFavourite("1", true)

		assert(result is Resource.Success)
		val updated = (result as Resource.Success).data
		assertThat(updated.isFavourite).isTrue()
		verify(dao).insert(entity.copy(isFavourite = true))
	}

	@Test
	fun toggleFavourite_returns_failed_if_breed_not_found() = runTest {
		whenever(dao.getById("404")).thenReturn(null)

		val result = repository.toggleFavourite("404", true)

		assert(result is Resource.Failed)
	}
}

