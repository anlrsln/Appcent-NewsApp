package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.entities.Article
import com.example.newsapp.databinding.FragmentDetailBinding
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.util.transition

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    lateinit var viewModel: NewsViewModel
    val bundle:DetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        viewModel = (activity as MainActivity).viewModel


        binding.detailPageToolbarTitle = "Details"

        //Bundle ile gelen article verisini tanımlama
        val article = bundle.article

        // Article verilerini view'lara gönderen metot
        setDetailPageContents(article)


        // Menu icon tıklama işlemleri
        binding.detailToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.shareItem -> {
                    true
                }
                R.id.favoriteItem ->{
                    viewModel.favoriteArticle(article)
                    Toast.makeText(requireContext(),"Article added to favorites.",Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }


        binding.newsSourceButton.setOnClickListener {
            val pass = DetailFragmentDirections.detailToWebView(article)
            Navigation.transition(it,pass)
        }

        return binding.root
    }

    fun setDetailPageContents(article:Article){
        Glide.with(binding.root).load(article.urlToImage).into(binding.newsImageView)
        binding.titleView.text = article.title
        binding.authorView.text = article.author
        binding.dateView.text = article.publishedAt
        binding.descriptionView.text = article.description
    }




}