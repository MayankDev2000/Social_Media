package com.mayank.socialmedia.Fragments.viewModel

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mayank.socialmedia.R
import com.mayank.socialmedia.data.database.cache
import com.mayank.socialmedia.data.repository.repository
import com.mayank.socialmedia.services.instance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class HomeViewModel
@Inject
constructor(val repo: repository): ViewModel(){

    val alldata = repo.liveData
    var list:List<cache> = mutableListOf(
        cache(Random.nextLong(),"Mayank","12345678","Hello from Mayank", Calendar.getInstance().timeInMillis),
        cache(Random.nextLong(),"Mayank","12345678","Hello from Mayank", Calendar.getInstance().timeInMillis),

    )

    fun addPost(context: Context){
        var view: View = View.inflate(context,R.layout.dialog,null)
        AlertDialog.Builder(context).setTitle("Add Post")
            .setView(view)
            .setPositiveButton("Post",DialogInterface.OnClickListener{ dialog, id ->
                val PostText:EditText = view.findViewById(R.id.postEditText)
                val value = PostText.text.toString().trim()
                viewModelScope.launch(Dispatchers.IO) {
                    instance.user!!.displayName?.let {
                        cache(Random.nextLong(), it,
                            instance.user!!.uid,value,Calendar.getInstance().timeInMillis)
                    }?.let { repo.insert(it,context) }
                }
            })
            .setNegativeButton("Cancel", { dialog, id ->
                dialog.cancel()
            })
            .show()
    }
}
