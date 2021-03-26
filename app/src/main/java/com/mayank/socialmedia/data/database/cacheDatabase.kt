package com.mayank.socialmedia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(cache::class,useruid::class),version = 1,exportSchema = false)
abstract class cacheDatabase:RoomDatabase() {
    abstract fun cacheDao():dao

}