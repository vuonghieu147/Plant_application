package com.example.app_f

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class ArticlesAdapter(private val articles: ArrayList<ArticlesData>): RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {
    class ViewHolder(item: View):RecyclerView.ViewHolder(item){
        val articlesImage: ImageView = item.findViewById(R.id.articles_image)
        val articlesTitle: TextView = item.findViewById(R.id.articles_title)
        val articlesPoster: TextView = item.findViewById(R.id.articles_poster)
        val layout: LinearLayout = item.findViewById(R.id.articles_button)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapter.ViewHolder {
        val itemView1 = LayoutInflater.from(parent.context).inflate(R.layout.item_articles,parent,false)
        val holder_1 = ArticlesAdapter.ViewHolder(itemView1)
        return holder_1
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articlesItem: ArticlesData = articles[position]
        holder.articlesTitle.text = articlesItem.titleArticles
        holder.articlesPoster.text = articlesItem.posterArticles
        Picasso.get()
            .load(articlesItem.imageArticles)
            .into(holder.articlesImage)
        holder.layout.setOnClickListener {
            val intent = Intent(holder.itemView.context, InfoArticles::class.java)
            intent.putExtra("documentId", articlesItem.titleArticles)
            holder.itemView.context.startActivity(intent)
        }
    }
}
