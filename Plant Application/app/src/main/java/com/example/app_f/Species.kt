package com.example.app_f

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore



class Species : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: SpeciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.species)

        db = FirebaseFirestore.getInstance()

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonBack = findViewById<ImageButton>(R.id.species_back)
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val collectionRef = db.collection("species")

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                val documentIds = ArrayList<String>()
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    documentIds.add(documentId)
                }
                // Tạo danh sách loài hoa từ danh sách document IDs
                val flowers = documentIds.map { FlowerName(flowerName = it, documentId = it) }
                adapter = SpeciesAdapter(flowers)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
            }
    }
}