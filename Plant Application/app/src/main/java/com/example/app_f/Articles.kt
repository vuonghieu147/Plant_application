package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Articles : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter1: ArticlesAdapter
    private val articlesList = ArrayList<ArticlesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articles)

        db = FirebaseFirestore.getInstance()

        val buttonBack = findViewById<ImageButton>(R.id.articles_back)
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_articles)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("articles")
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    val fieldValue1 = document.getString("Poster")
                    val fieldValue2 = document.getString("Image")
                    val article = ArticlesData(imageArticles = fieldValue2, titleArticles = documentId, posterArticles = fieldValue1)
                    articlesList.add(article)
                }
                adapter1 = ArticlesAdapter(articlesList)
                recyclerView.adapter = adapter1
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error retrieving image: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
