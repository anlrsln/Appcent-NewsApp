package com.example.newsapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.data.entities.Source

@Entity(
    tableName = "articles"

)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val source: Source? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
) : java.io.Serializable