package com.superapps.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.superapps.data.database.SwordCatsDataBase
import com.superapps.data.utils.createBreedEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BreedsDaoTest {

	private lateinit var db: SwordCatsDataBase
	private lateinit var dao: BreedsDao

	@Before
	fun setup() {
		db = Room.inMemoryDatabaseBuilder(
			ApplicationProvider.getApplicationContext(),
			SwordCatsDataBase::class.java
		)
			.allowMainThreadQueries()
			.build()
		dao = db.breedsDao()
	}

	@After
	fun tearDown() {
		db.close()
	}

	@Test
	fun insertAndGetById() = runTest {
		val breed = createBreedEntity("123", "Siamese")
		dao.insert(breed)

		val loaded = dao.getById("123")
		assertThat(loaded).isNotNull()
		assertThat(loaded?.name).isEqualTo("Siamese")
	}

	@Test
	fun insertAndGetAll() = runTest {
		val breeds = listOf(
			createBreedEntity("1", "Persian"),
			createBreedEntity("2", "Bengal")
		)
		dao.insertAll(breeds)

		val all = dao.getAll()
		assertThat(all).hasSize(2)
		assertThat(all.map { it.name }).containsExactly("Persian", "Bengal")
	}

	@Test
	fun getAllFavouritesReturnsOnlyFavourites() = runTest {
		val breeds = listOf(
			createBreedEntity("1", "Persian", isFavourite = true),
			createBreedEntity("2", "Bengal", isFavourite = false)
		)
		dao.insertAll(breeds)

		val favourites = dao.getAllFavourite().first()
		assertThat(favourites).hasSize(1)
		assertThat(favourites.first().name).isEqualTo("Persian")
	}

	@Test
	fun clearAllDeletesEverything() = runTest {
		dao.insert(createBreedEntity("1", "Persian"))
		dao.clearAll()

		val all = dao.getAll()
		assertThat(all).isEmpty()
	}

	@Test
	fun getPagedBreedsReturnsData() = runTest {
		val breeds = listOf(
			createBreedEntity("1", "Persian"),
			createBreedEntity("2", "Bengal")
		)
		dao.insertAll(breeds)

		val pagingSource = dao.getPagedBreeds()
		val loadResult = pagingSource.load(
			PagingSource.LoadParams.Refresh(
				key = null,
				loadSize = 2,
				placeholdersEnabled = false
			)
		)

		val page = loadResult as PagingSource.LoadResult.Page
		assertThat(page.data).hasSize(2)
		assertThat(page.data.first().name).isEqualTo("Bengal")
	}
}
