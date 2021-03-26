package com.mayank.socialmedia.data.repository

import android.content.Context
import android.util.Log
import com.mayank.socialmedia.data.api.FireStore
import com.mayank.socialmedia.data.database.cache
import com.mayank.socialmedia.data.database.dao
import com.mayank.socialmedia.data.database.useruid
import com.mayank.socialmedia.services.instance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class repository
@Inject
constructor(private val cacheDao: dao) {

    var liveData = cacheDao.getAllpost()
    var fire = FireStore
    suspend fun insert(cache: cache, context: Context){
        cacheDao.insert(cache)
        fire.savePost(context,cache)
    }
    suspend fun updatedata(){
        withContext(Dispatchers.IO){
            fire.getAllPost(cacheDao)
        }
    }

}
