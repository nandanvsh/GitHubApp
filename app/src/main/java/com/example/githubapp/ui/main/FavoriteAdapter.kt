package com.example.githubapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.Users
import com.example.githubapp.data.response.ItemsItem
import com.example.githubapp.database.Favorite
import com.example.githubapp.databinding.ItemRowUserBinding
import com.example.githubapp.helper.FavoriteDiffCallback
import com.example.githubapp.ui.DetailUserActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val listFavorite = ArrayList<Favorite>()

    fun setListFavorite(listFavorites: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorite, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }
    class FavoriteViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite){
            with(binding){
                tvItemNama.text = favorite.username
                Picasso.get().load(favorite.avatar).into(binding.imgItemPhoto)
                val user = ItemsItem(login = favorite.username!!, avatarUrl = favorite.avatar!! )
                val userJson = Gson().toJson(user)
                cardView.setOnClickListener{
                    val intentDetail = Intent(itemView.context, DetailUserActivity::class.java)
                    intentDetail.putExtra(DetailUserActivity.DETAIL_KEY, userJson)
                    itemView.context.startActivity(intentDetail)
                }
            }
        }
    }
}