package com.mayank.socialmedia.services

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object instance {
    val mAuth = FirebaseAuth.getInstance()
    var user: FirebaseUser ?= mAuth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    val Storage = FirebaseStorage.getInstance().getReference("userImage")
    val postdb = firestore.collection("post")
    var bool: MutableLiveData<Boolean> = MutableLiveData(true)
    var username:MutableLiveData<String> = MutableLiveData<String>("")
    val hasInternet:MutableLiveData<Boolean> = MutableLiveData(false)
    init {
        user?.displayName?.let {
            username.value = it
        }
    }
}