package com.example.githubapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.databinding.ItemRowUserBinding
import com.google.gson.Gson

class FollowAdapter : ListAdapter<DetailResponse, FollowAdapter.FollowViewHolder>(DIFF_FOLLOW_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val follow = getItem(position)
        holder.bind(follow)
        Glide.with(holder.itemView.context).load(follow.avatarUrl).into(holder.binding.imgItemPhoto)
    }
    class FollowViewHolder(val binding: ItemRowUserBinding ) : RecyclerView.ViewHolder(binding.root ) {
        fun bind(follow: DetailResponse){
            binding.tvItemNama.text = "${follow.login}"
            val userJson = Gson().toJson(follow)
            binding.cardView. setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailUserActivity::class.java)
                intentDetail.putExtra(DetailUserActivity.DETAIL_KEY, userJson)
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    companion object{
        val DIFF_FOLLOW_CALLBACK = object :  DiffUtil.ItemCallback<DetailResponse>(){
            override fun areItemsTheSame(oldItem: DetailResponse, newItem: DetailResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DetailResponse, newItem: DetailResponse): Boolean {
                return oldItem == newItem
            }

        }
    }


}