package com.example.app_f

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ArticlesLiked : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter:ArticlesLikedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles_liked)

        auth = FirebaseAuth.getInstance()
        val email = auth.currentUser?.email
        db = FirebaseFirestore.getInstance()

        val button = findViewById<ImageButton>(R.id.articles_liked_back)
        button.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_articles_liked)
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (email != null) {
            db.collection(email).document("Articles Liked").get().addOnSuccessListener {
                val documentIds = ArrayList<String>()
                val data = it.get("Articles Liked") as List<String>
                adapter = ArticlesLikedAdapter(data.map { ArticlesData(imageArticles = it, titleArticles = it, posterArticles = it) })
                recyclerView.adapter = adapter
            }
        }
    }
}