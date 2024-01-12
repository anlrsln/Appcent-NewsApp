package com.example.newsapp.util

import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.viewmodel.NewsViewModel

class SwipeLeftToDelete() {

    // Favori listesindeki article'ları sola kaydırıp silmemizi sağlayan sınıf
    companion object {
        fun setItemTouchHelper(adapter:NewsAdapter,viewModel: NewsViewModel,rv:RecyclerView){
            val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val article = adapter.differ.currentList[position]
                    viewModel.deleteNews(article)
                }
            }
            ItemTouchHelper(itemTouchHelper).apply {
                attachToRecyclerView(rv)
            }
        }
    }




}