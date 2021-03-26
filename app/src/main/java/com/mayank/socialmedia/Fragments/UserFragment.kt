package com.mayank.socialmedia.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.StorageReference
import com.mayank.socialmedia.R
import com.mayank.socialmedia.services.instance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UserFragment : Fragment() {

    lateinit var saveChange:Button
    lateinit var getimageButton : FloatingActionButton
    lateinit var setImage : ImageView
    lateinit var name:EditText
    val CODE_FOR_IMAGE:Int = 786
    lateinit var imagedata:Bitmap

    var file_name = "${instance.user?.uid}.jpeg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getimageButton = view.findViewById(R.id.changedp)
        saveChange = view.findViewById(R.id.savechages)
        setImage = view.findViewById(R.id.imageViewprofile)
        name = view.findViewById(R.id.usernameCh)
        getimageButton.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"),CODE_FOR_IMAGE)
        }
        saveChange.setOnClickListener { saveChangefun() }
        instance.username.observe(viewLifecycleOwner,{
            name.setText(it)
            var user = UserProfileChangeRequest.Builder().setDisplayName(it).build()
            instance.user?.updateProfile(user)
        })
        BitmapFactory.decodeFile("${activity?.cacheDir}/$file_name")?.let {
            imagedata = it
            setImage.setImageBitmap(imagedata)
        }
    }

    private fun saveChangefun() {
      if(::imagedata.isInitialized)
        GlobalScope.launch(Dispatchers.IO) {
            val baos = ByteArrayOutputStream()
            imagedata.compress(Bitmap.CompressFormat.JPEG,100,baos)
            savetodir()
            val refrence = instance.Storage.child(file_name)
            refrence.putBytes(baos.toByteArray()).addOnSuccessListener {
                DownloadUri(refrence)
                Log.d("success","successfull")
            }.addOnFailureListener{
                Log.e("error",it.toString())
            }
        }
        instance.username.value = name.text.toString()
        findNavController().navigate(R.id.action_user_to_home)
        instance.bool.value = true
    }

    fun savetodir(){
        var f = File(context?.cacheDir,file_name)
        f.createNewFile()
        Log.d("user path",f.absolutePath.toString())
        var baos = ByteArrayOutputStream()
        imagedata.compress(Bitmap.CompressFormat.JPEG,100,baos)
        Log.d("auth path",f.absolutePath.toString())
        var filewriter = FileOutputStream(f)
        filewriter.write(baos.toByteArray())
        filewriter.flush()
        filewriter.close()
    }

    fun DownloadUri(storage:StorageReference){
        storage.downloadUrl.addOnSuccessListener {
            var user = UserProfileChangeRequest.Builder().setPhotoUri(it).build()
            instance.user?.updateProfile(user)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CODE_FOR_IMAGE){
            if (data != null) {
                try {
                    var cr = activity?.contentResolver?.openInputStream(data.data as Uri)
                    imagedata = BitmapFactory.decodeStream(cr)
                    if (cr != null) {
                        cr.close()
                    }
                    imagedata = Bitmap.createScaledBitmap(imagedata,480,480,false)
                    setImage.setImageBitmap(imagedata)
                }catch (e:Exception){
                    Log.d("error",e.toString())
                }
            }else{
                Toast.makeText(context,"null",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

