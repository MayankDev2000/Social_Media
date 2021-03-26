package com.mayank.socialmedia.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

object InternetCheck {
    suspend fun checkInternet(){
        withContext(Dispatchers.IO){
            try {
                var socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8",53),1500)
                socket.close()
                instance.hasInternet.value = true
            }catch (e:Exception){
                instance.hasInternet.value = false
            }
        }
    }
}