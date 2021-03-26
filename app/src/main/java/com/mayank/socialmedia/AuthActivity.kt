package com.mayank.socialmedia

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mayank.socialmedia.services.instance
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AuthActivity : AppCompatActivity() {

    private var RC_SIGN_IN:Int = 123
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }
    }
    private fun signIn() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient =  GoogleSignIn.getClient(this,gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        Handler().postDelayed({
            splash.performClick()
        },1000)
        googleSignInButton.setOnClickListener {
            signIn()
        }
    }

    fun firebaseAuthWithGoogle(idtoken:String){
        val credential = GoogleAuthProvider.getCredential(idtoken,null)
        instance.mAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                instance.user = FirebaseAuth.getInstance().currentUser
                    setupUser()
                Toast.makeText(this,"success", Toast.LENGTH_SHORT).show()
                finishAffinity()
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                Toast.makeText(this,"SignIn Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

     fun setupUser(){

        instance.user?.displayName?.let {
            instance.username.value = it
        }
        instance.user?.photoUrl?.let {
            var file_name = "${instance.user?.uid}.jpeg"
            Log.d("image",it.toString())
            var f = File(applicationContext.cacheDir,file_name)
            Log.d("auth path",f.absolutePath.toString())
            var temp = File.createTempFile("ppdd",".jpeg")
            instance.Storage.child(file_name).getFile(temp).addOnSuccessListener {
                var baos = ByteArrayOutputStream()
                val bit:Bitmap = BitmapFactory.decodeFile(temp.absolutePath)
                bit.compress(Bitmap.CompressFormat.JPEG,100,baos)
                var writer = FileOutputStream(f)
                writer.write(baos.toByteArray())
                writer.flush()
                writer.close()
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,"error "+e,Toast.LENGTH_SHORT).show()
                // ...
            }
        }
    }

}