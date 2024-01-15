package com.example.newsapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.entities.Article
import com.example.newsapp.databinding.CardArticleBinding
import com.example.newsapp.ui.fragment.FavoritesFragment
import com.example.newsapp.ui.fragment.FavoritesFragmentDirections
import com.example.newsapp.ui.fragment.MainPageFragment
import com.example.newsapp.ui.fragment.MainPageFragmentDirections
import com.example.newsapp.util.Constants.Companion.NO_IMAGE
import com.example.newsapp.util.transition

class NewsAdapter(var mContext:Context,var fragment: Fragment) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(var binding:CardArticleBinding):RecyclerView.ViewHolder(binding.root)


    //AsyncListDiffer, eski ve yeni bir liste arasındaki farkları hesaplayarak listeleri günceller
    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding:CardArticleBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.card_article,parent,false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList.get(position)
        holder.binding.apply {
            Glide.with(this.cardView).load(article.urlToImage?:NO_IMAGE).into(this.imageView)
            this.titleView.text = article.title
            this.descriptionView.text = article.description
            this.cardView.setOnClickListener {
                setTransitions(fragment,it,article)
            }
        }
    }


    // MainPageFragment ve FavoritesFragment sayfalarında aynı adapter'ı kullanıyoruz
    // Card'a tıklayınca hangi fragment'tan gittiğini bu metotla bildiriyoruz (MainPageFragmentDirections-FavoritesDirections)
    private fun setTransitions(fragment:Fragment,view: View,article: Article){
        when(fragment){
            is MainPageFragment ->{
                val pass = MainPageFragmentDirections.mainToDetail(article)
                Navigation.transition(view,pass)
            }
            is FavoritesFragment ->{
                val pass = FavoritesFragmentDirections.favoriteToDetail(article)
                Navigation.transition(view,pass)
            }
        }
    }



}