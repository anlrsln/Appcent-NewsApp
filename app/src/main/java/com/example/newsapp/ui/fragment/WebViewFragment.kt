package com.example.newsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailBinding
import com.example.newsapp.databinding.FragmentWebViewBinding
import com.example.newsapp.ui.viewmodel.NewsViewModel


class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    lateinit var viewModel: NewsViewModel
    val bundle:DetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_web_view,container,false)
        viewModel = (activity as MainActivity).viewModel

        binding.webViewPageToolbarTitle = "Source"

        val article = bundle.article

        binding.webView.apply {
            // Sayfayı her zaman webView içerisinde yüklenmesi ve browser'da açılmaması için
            webViewClient = WebViewClient()
            loadUrl(article.url?:"Source Error")

        }


        return binding.root
    }

}