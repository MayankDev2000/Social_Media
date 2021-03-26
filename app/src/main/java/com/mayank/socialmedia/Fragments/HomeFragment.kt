package com.mayank.socialmedia.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mayank.socialmedia.Fragments.utils.HomeAdapterRecycler
import com.mayank.socialmedia.Fragments.utils.onPostClick
import com.mayank.socialmedia.Fragments.viewModel.HomeViewModel
import com.mayank.socialmedia.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(),onPostClick {


    lateinit var Recycler:RecyclerView;
    lateinit var adapter:HomeAdapterRecycler;
    lateinit var addPostbutton:FloatingActionButton;
    private val viewModel :HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_home, container, false)
        addPostbutton = view.findViewById(R.id.addPost)
        Recycler = view.findViewById(R.id.HomeRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPostbutton.setOnClickListener {
            context?.let { it1 -> viewModel.addPost(it1) }
        }
        savedInstanceState?.putInt("position",Recycler.scrollState)
        if(savedInstanceState != null){
            Recycler.scrollToPosition(savedInstanceState.getInt("position"))
        }
        setRecyclerView(viewModel)
       viewModel.alldata.asLiveData().observe(viewLifecycleOwner,{
           it?.let {
               adapter.onDataChange(it)
           }
       })
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.repo.updatedata()
        }
        if(context?.let { ContextCompat.checkSelfPermission(it,Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED) {

        }else{
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111){
            if (grantResults.isNotEmpty() && (grantResults[0]==PackageManager.PERMISSION_GRANTED)){

            }else{
                activity?.finishAffinity()
            }
        }
    }

    fun setRecyclerView(viewModel:HomeViewModel){
        var layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        adapter = HomeAdapterRecycler(this,viewModel.list,context)
        adapter.onDataChange(viewModel.list)
        Recycler.layoutManager =  layoutManager
        Recycler.adapter = adapter
    }


    override fun onPostClick(post: String) {
        Toast.makeText(context,post,Toast.LENGTH_SHORT).show()
    }
}