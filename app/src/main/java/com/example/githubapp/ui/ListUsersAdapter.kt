package com.example.githubapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.databinding.ItemRowUserBinding
import com.google.gson.Gson
import kotlinx.coroutines.withContext

class ListUsersAdapter : ListAdapter<ItemsItem,ListUsersAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        Glide.with(holder.itemView.context).load(user.avatarUrl).into(holder.binding.imgItemPhoto)
    }
    class ListViewHolder(val binding: ItemRowUserBinding ) : RecyclerView.ViewHolder(binding.root ) {
        fun bind(user: ItemsItem){
            binding.tvItemNama.text = "${user.login}"
            val userJson = Gson().toJson(user)
            binding.cardView. setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailUserActivity::class.java)
                intentDetail.putExtra(DetailUserActivity.DETAIL_KEY, userJson)
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object :  DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}