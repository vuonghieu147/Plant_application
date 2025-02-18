package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class InfoArticles : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_articles)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val titleArticles = findViewById<TextView>(R.id.info_title_articles)
        val dayArticles = findViewById<TextView>(R.id.info_day_articles)
        val posterArticles = findViewById<TextView>(R.id.info_poster_articles)
        val infoArticles = findViewById<TextView>(R.id.info_detail_articles)
        val imageArticles = findViewById<ImageView>(R.id.info_image_articles)


        val buttonBack = findViewById<ImageButton>(R.id.info_back_articles)
        buttonBack.setOnClickListener {
            onBackPressed()
        }
        val intent = intent
        val documentId = intent.getStringExtra("documentId")

        titleArticles.text = documentId

        val collectionRef = db.collection("articles")


        if (documentId != null) {
            collectionRef.document(documentId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val day = documentSnapshot.getString("Day")
                        val info = documentSnapshot.getString("Info")
                        val poster = documentSnapshot.getString("Poster")
                        val image = documentSnapshot.getString("Image")
                        dayArticles.text = day
                        posterArticles.text = poster
                        infoArticles.text = info
                        Glide.with(this)
                            .load(image)
                            .into(imageArticles)

                    } else {
                        Toast.makeText(this,"Error retrieving data!!",Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this,"Error!!",Toast.LENGTH_SHORT).show()
                }
        }
        var like_Flag: Boolean = true
        val like = findViewById<ImageButton>(R.id.info_like_articles)
        like.setOnClickListener {
            if (like_Flag) {
                like.setImageResource(R.drawable.baseline_thumb_up_alt_24)

                val name_Flow = documentId
                val currentUser = auth.currentUser
                val email = currentUser?.email
                val add = name_Flow

                if (email != null) {
                    db.collection(email).document("articles").update("Name", FieldValue.arrayUnion(add))
                }

            } else {
                like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                val name_Flow = documentId
                val currentUser = auth.currentUser
                val email = currentUser?.email
                if (email != null) {
                    db.collection(email).document("articles")

                    val remove = name_Flow
                    db.collection(email).document("articles").update("Name", FieldValue.arrayRemove(remove))
                }
            }
            like_Flag = !like_Flag
        }
    }
}