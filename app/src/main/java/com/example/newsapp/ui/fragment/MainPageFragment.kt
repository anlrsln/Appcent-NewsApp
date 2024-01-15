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

        setScrollListener()

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
                        adapter.differ.submitList(newsResponse.articles.toList())
                        // Tüm sonuçları al / Bir sayfada bulunan sonuçlara böl + 2
                        // +2 -> (newsResponse.totalResults / QUERY_PAGE_SIZE küsüratlı
                        // çıkacağı için +1 ekledik, son sayfa her zaman boş olacağı için +1
                        // daha ekledik. Bu şekilde toplam tüm sayfa sayısını hesapladık
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            binding.recyclerView.setPadding(0,0,0,0)
                        }
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

        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        adapter.differ.submitList(newsResponse.articles.toList())
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
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    // Sayfanın sonuna gelindiğini tespit etme ve istek atma

    // yükleniyor durumunu öğrenmek için kullanacağımız değişken
    var isLoading = false
    // son sayfada olduğumuzu tespit edip pagination'u durdurmak için kullanacağımız değişken
    var isLastPage = false
    // anlık kaydırma işlemi yapılıyor mu tespit etmek için oluşturduğumuz değişken
    var isScrolling = false

    // Recyclerview için oluşturduğumuz scrollListener
    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // Anlık kaydırma işlemi yapılıyor mu kontrol eder
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // Burada sayfanın sonuna ulaştığımızı gösterebilecek bir mekanizma
            // olmadığı için kendimiz oluşturduk
            // Layoutmanager'a erişme
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            // ilk gözüken item'ın pozisyonunu alma
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            // Totalde visible olan item'ların sayısı
            val visibleItemCount = layoutManager.childCount
            // Recyclerview içerisindeki tüm item'ların sayısı
            val totalItemCount = layoutManager.itemCount

            // Bu üç değişkenle sayfanın sonunda olduğumuz durumu hesaplayabiliriz

            // Loading durumu yoksa ve son sayfada değilsek
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            // Son item'da olma durumunu hesaplama
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            // İlk item'dan aşağı kaydırma durumu (firs item visible değil)
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            // Bir sayfada yüklenen item sayısıyla, Recyclerview'daki item sayısı
            // eşit mi kontrolü
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            // Tüm bu durumları kullanarak sayfanın paginate işlemini yaptırırız
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            // Eğer paginate durumu oluşuyorsa yeni istek atarız
            if(shouldPaginate){
                viewModel.getBreakingNews(COUNTRY_CODE)
                isScrolling = false
            }
        }

    }


    fun setScrollListener(){
        binding.recyclerView.addOnScrollListener(this@MainPageFragment.scrollListener)
    }



    // ana sayfa tekrar açıldığında istek atar
    override fun onResume() {
        super.onResume()
        viewModel.getBreakingNews(COUNTRY_CODE)
    }




}