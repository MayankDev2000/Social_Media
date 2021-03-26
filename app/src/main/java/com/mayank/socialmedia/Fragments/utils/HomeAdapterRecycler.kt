package com.mayank.socialmedia.Fragments.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mayank.socialmedia.R
import com.mayank.socialmedia.data.database.cache
import com.mayank.socialmedia.services.instance
import java.text.DateFormat
import java.util.*

class HomeAdapterRecycler(var click: onPostClick,var data:List<cache>,var context : Context?): RecyclerView.Adapter<HomeAdapterRecycler.viewHolder>() {
    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var Posttext:TextView = itemView.findViewById(R.id.HomePostView)
        var ProfileImage : ImageView = itemView.findViewById(R.id.ProfileImage)
        var ProfileName :TextView = itemView.findViewById(R.id.ProfileName)
        var createdAt :TextView = itemView.findViewById(R.id.HomeCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.home_recycle_card_view,parent,false)
        val viewholder  = viewHolder(view)
        view.setOnClickListener {
            click.onPostClick(data.get(viewholder.adapterPosition).post)
        }
        return viewholder
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val post: cache = data.get(position)
        val ref = instance.Storage.child("${post.userimage}.jpeg")
        holder.Posttext.text = post.post
        holder.ProfileName.text = post.username
        holder.createdAt.text = Date(post.createdat).toString()
        context?.let {
            Glide.with(it)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ProfileImage) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun onDataChange(newData :List<cache>){
        data = newData
        notifyDataSetChanged()
    }

}
interface onPostClick{
    fun onPostClick(post:String)
}