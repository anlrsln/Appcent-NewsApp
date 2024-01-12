package com.example.newsapp.data.repository

import com.example.newsapp.data.entities.Article
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.retrofit.RetrofitInstance

class NewsRepository(val db:ArticleDatabase){

    suspend fun getBreakingNews(countryCode:String,page:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,page)


    suspend fun searchNews(searchQuery:String, page:Int) =
        RetrofitInstance.api.searchNews(searchQuery,page)


    suspend fun upsert(article:Article) = db.getArticleDao().upsert(article)


    fun getFavoriteNews() = db.getArticleDao().getAllArticles()


    suspend fun deleteNews(article: Article) = db.getArticleDao().deleteArticle(article)

}