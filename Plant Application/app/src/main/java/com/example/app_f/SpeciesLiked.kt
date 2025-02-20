package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SpeciesLiked : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SpeciesLikedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_species_liked)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        val speciesLikedBackButton = findViewById<ImageButton>(R.id.species_liked_back)
        speciesLikedBackButton.setOnClickListener {
            onBackPressed()
        }
        recyclerView = findViewById(R.id.recycler_view_species_liked)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SpeciesLikedAdapter(emptyList())
        recyclerView.adapter = adapter

        val currentUser = auth.currentUser
        val email = currentUser?.email
        if (email != null) {
            db.collection(email).document("Species Liked").get()
                .addOnSuccessListener {
                    val data = it.get("Species Liked") as? List<String>
                    val documentIds = ArrayList<String>()
                    Log.d("CHECK","$data")
                    if (data != null) {
                        for (document in data) {
                            documentIds.add(document)
                        }
                        val flowers = documentIds.map { SpeciesLikedData(nameSpeciesLiked = it, documentId = it) }
                        adapter = SpeciesLikedAdapter(flowers)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.adapter = adapter
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this,"Error getting document: ${exception.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }

}