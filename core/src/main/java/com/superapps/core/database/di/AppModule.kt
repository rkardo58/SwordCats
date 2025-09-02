package com.superapps.core.database.di

import android.app.Application
import androidx.room.Room
import com.superapps.core.database.SwordCatsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSwordDatabase(app: Application): SwordCatsDataBase =
        Room
            .databaseBuilder(
                app,
                SwordCatsDataBase::class.java,
                "sword_cats_db",
            ).fallbackToDestructiveMigration(true)
            .build()
}
