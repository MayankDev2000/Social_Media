package com.mayank.socialmedia.data.api

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.EventListener
import com.mayank.socialmedia.data.database.cache
import com.mayank.socialmedia.data.database.dao
import com.mayank.socialmedia.services.instance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

object FireStore {

    suspend fun savePost(context: Context,cache: cache){
        instance.postdb.add(cache).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Post Posted",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Post not posted yet"+it.exception,Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun getAllPost(dao:dao){
            instance.postdb.get().addOnCompleteListener {
                if(it.isSuccessful){
                    for(q in it.result!!){
                        GlobalScope.launch(Dispatchers.IO) {
                            dao.insert(cache(q.get("postid") as Long,
                                q.get("username") as String,
                                q.get("userimage") as String,
                                q.get("post") as String,
                                q.get("createdat") as Long))
                        }
                    }
                }
            }
    }
}