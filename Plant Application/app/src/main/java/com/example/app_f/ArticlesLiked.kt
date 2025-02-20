package com.example.app_f

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ArticlesLiked : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ArticlesLikedAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_articles_liked)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email = currentUser?.email
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_articles_liked)
        val backButton = findViewById<ImageButton>(R.id.articles_liked_back)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        db = FirebaseFirestore.getInstance()
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (email != null) {
            db.collection(email).document("Articles Liked").get().addOnSuccessListener {


            }
        }
    }
}