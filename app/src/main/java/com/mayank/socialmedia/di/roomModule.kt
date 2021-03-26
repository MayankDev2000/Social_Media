package com.mayank.socialmedia.di

import android.content.Context
import androidx.room.Room
import com.mayank.socialmedia.data.database.cacheDatabase
import com.mayank.socialmedia.data.database.dao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object roomModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context):cacheDatabase{
        return Room.databaseBuilder(context,
            cacheDatabase::class.java,
            "cache_Database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db : cacheDatabase):dao{
        return db.cacheDao()
    }
}