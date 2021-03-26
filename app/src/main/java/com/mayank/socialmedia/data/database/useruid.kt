package com.mayank.socialmedia.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class useruid(@ColumnInfo(name = "uid")var uid:String ) {
    @PrimaryKey(autoGenerate = true)var id:Int = 0
}