package com.example.app_f

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ArticlesLikedAdapter(private val articlesList: List<ArticlesData>)
    : RecyclerView.Adapter<ArticlesLikedAdapter.ViewHolder>() {
    class ViewHolder(item: View):RecyclerView.ViewHolder(item){
        val articlesImage: ImageView = item.findViewById(R.id.articles_image)
        val articlesTitle: TextView = item.findViewById(R.id.articles_title)
        val articlesPoster: TextView = item.findViewById(R.id.articles_poster)
        val layout: LinearLayout = item.findViewById(R.id.articles_button)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticlesLikedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_articles,parent,false)
        return ArticlesLikedAdapter.ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ArticlesLikedAdapter.ViewHolder, position: Int) {
        val item = articlesList[position]
        holder.articlesTitle.text = item.titleArticles
        holder.articlesPoster.text = item.posterArticles
        Picasso.get().load(item.imageArticles).into(holder.articlesImage)
        holder.layout.setOnClickListener{
            val intent = Intent(holder.itemView.context, InfoArticles::class.java)
            intent.putExtra("documentId",item.titleArticles)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }
}