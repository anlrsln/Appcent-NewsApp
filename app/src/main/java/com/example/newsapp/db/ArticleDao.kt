package com.example.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.data.entities.Article

@Dao
interface ArticleDao {

    //article, database'e insert edilir, eğer zaten varsa da update edilir
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article:Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}