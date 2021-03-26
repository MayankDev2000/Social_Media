package com.mayank.socialmedia

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.view.SurfaceControl
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.mayank.socialmedia.services.instance
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    var t : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val navControl = findNavController(R.id.FragmentNavHost)
        toolbaraccount.setOnClickListener {
            instance.bool.value = false
            navControl.navigate(R.id.action_home_to_user)
        }
        instance.bool.observe(this,{
            if(it)toolbaraccount.visibility = View.VISIBLE
            else toolbaraccount.visibility = View.GONE
        })
        instance.hasInternet.observe(this,{
            if(!it){
                Snackbar.make(findViewById(android.R.id.content),"Internet Connection Has Lost",Snackbar.LENGTH_LONG)
                    .show()
            }
        })

    }



    override fun onBackPressed() {
        Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT).show()
        t = t + 1000
        if(t>=2000){
            finishAffinity()
        }else{
            super.onBackPressed()
            instance.bool.value = true
        }
        postDelayed(Handler(),{
            t = 0;
        },"111",2000)
    }



}

