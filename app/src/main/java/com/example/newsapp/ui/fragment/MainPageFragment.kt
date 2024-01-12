package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.entities.Article
import com.example.newsapp.databinding.FragmentMainPageBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.util.Constants.Companion.COUNTRY_CODE
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.util.Resource

class MainPageFragment : Fragment() {

    private lateinit var binding: FragmentMainPageBinding
    lateinit var viewModel: NewsViewModel
    lateinit var adapter:NewsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_page,container,false)

        viewModel = (activity as MainActivity).viewModel
        adapter = NewsAdapter(requireContext(),this@MainPageFragment)

        binding.adapter=adapter
        binding.mainPageToolbarTitle  = "Appcent News App"

        // haberler listesi live data
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse ->  
                        adapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message->
                        Log.e("MainPageFragment","An error occured : ${message}")
                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })

        // arama yapılan kelimeleri dinler, kelimeleri viewmodel'daki arama fonksiyonuna gönderir
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchNews(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.getSearchNews(newText)
                return true
            }
        })

        // Searchview'daki close butonunu dinler, tekrar breaking news verilerini almak için istek atar
        binding.searchView.setOnCloseListener {
            viewModel.getBreakingNews(COUNTRY_CODE)
            true
        }


        return binding.root
    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }


    // ana sayfa tekrar açıldığında istek atar
    override fun onResume() {
        super.onResume()
        viewModel.getBreakingNews(COUNTRY_CODE)
    }




}