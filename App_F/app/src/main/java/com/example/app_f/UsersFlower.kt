package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsersFlower : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: UsersFlowerAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_flower)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_usersflower)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonBack = findViewById<ImageButton>(R.id.usersflower_back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        val currentUser = auth.currentUser
        val email = currentUser?.email
        if(email != null) {
            val collectionRef = db.collection(email).document("User's Flower").collection("species")
            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val documentIds = ArrayList<String>()
                    for (document in querySnapshot.documents) {
                        val documentId = document.id
                        documentIds.add(documentId)
                    }
                    val flowers = documentIds.map { UsersFlowerData(it, it) }
                    adapter = UsersFlowerAdapter(flowers)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}