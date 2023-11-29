package com.example.githubapp

import android.app.Application
import android.os.Bundle
import android.text.style.TtsSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.data.response.DetailResponse
import com.example.githubapp.databinding.FragmentFollowersBinding
import com.example.githubapp.factory.ViewModelFactory
import com.example.githubapp.ui.DetailUserActivity
import com.example.githubapp.ui.DetailViewModel
import com.example.githubapp.ui.FollowAdapter
import com.example.githubapp.ui.FollowersViewModel

class FollowersFragment() : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private var username: String? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userFollowersModel = obtainViewModel()
        arguments?.let {
            username = it.getString(ARG_USERNAME)
            position = it.getInt(ARG_POSITION)
        }
        if (username != null){
            if (position==1){
                userFollowersModel.findFollowing(username!!)
            }else{
                userFollowersModel.findFollowers(username!!)
            }
        }

        if (position==1){
            userFollowersModel.listFollowing.observe(this) {
                setUserData(it)
            }
        }else{
            userFollowersModel.listFollowers.observe(this) {
                setUserData(it)
            }
        }

        userFollowersModel.isLoading.observe(this){
            showLoading(it)
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.layoutManager = layoutManager
        binding.rvFollowers.isNestedScrollingEnabled = true;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUserData(listFollowers: List<DetailResponse>?){
        val adapter = FollowAdapter()
        adapter.submitList(listFollowers)
        binding.rvFollowers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val ARG_POSITION = "POSTITION_KEY"
        const val ARG_USERNAME = "USERNAME_KEY"
    }

    private fun obtainViewModel(): FollowersViewModel {
        val factory = ViewModelFactory.getInstance(requireActivity().application, null)
        return ViewModelProvider(requireActivity(), factory).get(FollowersViewModel::class.java)
    }

}