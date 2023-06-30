package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
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
    private val nameList: MutableList<SpeciesLikedData> = mutableListOf()
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
        adapter = SpeciesLikedAdapter(emptyList()) // Pass an empty list initially
        recyclerView.adapter = adapter
        val currentUser = auth.currentUser
        val email = currentUser?.email
        if (email != null) {
            db.collection(email).document("species").get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val dataMap = documentSnapshot.data
                        if (dataMap != null) {
                            for ((key, value) in dataMap) {
                                if (key.startsWith("Name")) {
                                    val name = value as String
                                    val data = SpeciesLikedData(name)
                                    nameList.add(data)
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this,"Error getting document: ${exception.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }

}