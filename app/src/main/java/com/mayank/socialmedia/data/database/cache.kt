package com.mayank.socialmedia.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache")
class cache (@PrimaryKey var postid:Long,
             @ColumnInfo(name = "name" )var username:String,
             @ColumnInfo(name = "image" )var userimage: String,
             @ColumnInfo(name = "post" )var post:String,
             @ColumnInfo(name = "date" )var createdat:Long)