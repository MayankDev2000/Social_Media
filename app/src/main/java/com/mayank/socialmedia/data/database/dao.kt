package com.mayank.socialmedia.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cache: cache)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertuid(uid:useruid)

    @Query("SELECT * from user")
    fun getAlluser():Flow<List<useruid>>

    @Query("SELECT * from cache")
    fun getAllpost():Flow<List<cache>>
}