package com.example.app_f

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class InfoSpecies : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var detail: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_species)

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val currentUser = auth.currentUser
        val email = currentUser?.email
        val buttonBack = findViewById<ImageButton>(R.id.infospecies_back)
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        title = findViewById(R.id.info_name)
        val intent = intent
        val documentId = intent.getStringExtra("documentId")
        title.text = documentId

        val documentRef = db.collection("species").document(documentId!!)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val fieldValue = document.getString("Description")
                    detail = findViewById(R.id.info_detail)
                    detail.text = fieldValue
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
            }
        val imageView: ImageView = findViewById(R.id.info_image)
        val folderRef = storageRef.child("Species")
        folderRef.listAll()
            .addOnSuccessListener { listResult ->
                for (fileRef in listResult.items) {
                    val fileName = fileRef.name
                    if (fileName.startsWith(documentId)) {
                        fileRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                // Tải ảnh từ URL và gán vào ImageView bằng Picasso
                                Picasso.get().load(uri).into(imageView)
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "ERROR!!!", Toast.LENGTH_SHORT).show()
                            }
                        break // Dừng vòng lặp sau khi tìm thấy tệp tin phù hợp
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "ERROR Folder!!!", Toast.LENGTH_SHORT).show()
            }


        val like = findViewById<ImageButton>(R.id.info_like_species)
        like.setOnClickListener {
            val currImage: Any? = like.tag as? Int
            if (currImage == R.drawable.baseline_thumb_up_alt_24) {
                like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
                if (email != null) {
                    db.collection(email).document("Species Liked")
                        .update("Species Liked", FieldValue.arrayRemove(documentId))
                }
            }
            else if(currImage == R.drawable.baseline_thumb_up_off_alt_24){
                like.setImageResource(R.drawable.baseline_thumb_up_alt_24)
                if (email != null) {
                    db.collection(email).document("Species Liked")
                        .update("Species Liked", FieldValue.arrayUnion(documentId))
                }
            }
        }
        if (email != null) {
            db.collection(email).document("Species Liked").get().addOnSuccessListener {
                val data = it.get("Species Liked") as? List<String> ?: listOf()
                Log.d("CHECK", "$data")
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