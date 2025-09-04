package com.superapps.data.di

import android.app.Application
import androidx.room.Room
import com.superapps.data.database.SwordCatsDataBase
import com.superapps.data.database.dao.BreedsDao
import com.superapps.data.network.CatsApi
import com.superapps.data.repository.BreedsRemoteMediator
import com.superapps.data.repository.BreedsRepositoryImpl
import com.superapps.domain.repository.BreedsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

	@Provides
	@Singleton
	fun provideSwordDatabase(app: Application): SwordCatsDataBase = Room
		.databaseBuilder(
			app,
			SwordCatsDataBase::class.java,
			"sword_cats_db"
		).fallbackToDestructiveMigration(true)
		.build()

	@Provides
	@Singleton
	fun provideCatBreedDao(database: SwordCatsDataBase): BreedsDao = database.breedsDao()

	@Provides
	@Singleton
	fun provideBreedsRepository(api: CatsApi, dao: BreedsDao, remoteMediator: BreedsRemoteMediator): BreedsRepository =
		BreedsRepositoryImpl(api, dao, remoteMediator)
}
