package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class InfoArticles : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_articles)

        val db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val titleArticles = findViewById<TextView>(R.id.info_title_articles)
        val dayArticles = findViewById<TextView>(R.id.info_day_articles)
        val posterArticles = findViewById<TextView>(R.id.info_poster_articles)
        val infoArticles = findViewById<TextView>(R.id.info_detail_articles)
        val imageArticles = findViewById<ImageView>(R.id.info_image_articles)


        val buttonBack = findViewById<ImageButton>(R.id.info_back_articles)
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val currentUser = auth.currentUser
        val email = currentUser?.email
        val intent = intent
        val documentId = intent.getStringExtra("documentId")
        Log.d("CHECK", "$documentId & $email")

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
        val like = findViewById<ImageButton>(R.id.info_like_articles)
        like.setOnClickListener {
            val currImage: Any? = like.tag as? Int
            Log.d("CHECK", "Current tag: $currImage")
            if (currImage == R.drawable.baseline_thumb_up_alt_24) {
                like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                like.setTag(R.drawable.baseline_thumb_up_off_alt_24)
                if (email != null) {
                    db.collection(email).document("Articles Liked")
                        .update("Articles Liked", FieldValue.arrayRemove(documentId))
                }
            }
            else if(currImage == R.drawable.baseline_thumb_up_off_alt_24){
                like.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                like.setTag(R.drawable.baseline_thumb_up_alt_24)
                if (email != null) {
                    db.collection(email).document("Articles Liked")
                        .update("Articles Liked", FieldValue.arrayUnion(documentId))
                    Log.d("CHECK", "empty")
                }
            }
        }
        if (email != null) {
            db.collection(email).document("Articles Liked").get().addOnSuccessListener {
                val data = it.get("Articles Liked") as? List<String> ?: listOf()
                Log.d("CHECK", "Articles data: $data")
                if (documentId in data) {
                    like.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                    like.setTag(R.drawable.baseline_thumb_up_alt_24)
                } else if (data.isEmpty() || documentId !in data) {
                    like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                    like.setTag(R.drawable.baseline_thumb_up_off_alt_24)
                }
            }
        }
    }
}