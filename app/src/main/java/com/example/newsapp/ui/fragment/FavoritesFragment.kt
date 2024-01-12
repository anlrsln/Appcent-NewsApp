package com.example.newsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.util.SwipeLeftToDelete


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    lateinit var viewModel: NewsViewModel
    lateinit var adapter:NewsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorites,container,false)
        viewModel = (activity as MainActivity).viewModel


        adapter = NewsAdapter(requireContext(),this@FavoritesFragment)

        //  UI işlemleri için fragment_favorites'a adapter ve title gönderimi
        binding.adapter=adapter
        binding.favoritePageToolbarTitle = "Favorites"



        // Favori listesindeki article'ları sola kaydırıp silmemizi sağlayan metot
        SwipeLeftToDelete.setItemTouchHelper(adapter,viewModel,binding.recyclerView)

        // Favorilediğimiz article live data
        viewModel.getFavoriteArticles().observe(viewLifecycleOwner, Observer {articles ->
            adapter.differ.submitList(articles)
        })


        return binding.root
    }



}