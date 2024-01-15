package com.example.newsapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.entities.Article
import com.example.newsapp.databinding.FragmentDetailBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel
import com.example.newsapp.util.Constants.Companion.NO_IMAGE
import com.example.newsapp.util.Constants.Companion.UNKNOWN_SOURCE_RESPONSE
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

        // WebView yönlendirmesi
        binding.newsSourceButton.setOnClickListener {
            val pass = DetailFragmentDirections.detailToWebView(article)
            Navigation.transition(it,pass)
        }

        // Detail sayfasındaki back butonu metodları
        binding.detailToolbar.setNavigationIcon(R.drawable.arrow_back)
        binding.detailToolbar.setNavigationOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                requireActivity().onBackPressed() // Implemented by activity
            }
        })
        return binding.root
    }



    fun setDetailPageContents(article:Article){
        Glide.with(binding.root).load(article.urlToImage?:NO_IMAGE).into(binding.newsImageView)
        binding.titleView.text = article.title
        binding.authorView.text = article.author?: UNKNOWN_SOURCE_RESPONSE
        binding.dateView.text = article.publishedAt?:UNKNOWN_SOURCE_RESPONSE
        binding.descriptionView.text = article.description
    }




}