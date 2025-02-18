package com.example.app_f

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class UserFlowerInfo : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var detail: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_flower_info)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val buttonBack = findViewById<ImageButton>(R.id.info_usersflower_back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }
        val currentUser = auth.currentUser
        val email = currentUser?.email
        title = findViewById(R.id.usersflower_name)
        val intent = intent
        val documentId = intent.getStringExtra("documentId")
        title.text = documentId

        if(email!=null) {
            val documentRef = db.collection(email).document("User's Flower").collection("species").document(documentId!!)

            documentRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val fieldValue = document.getString("description")
                        detail = findViewById(R.id.info_usersflower_detail)
                        detail.text = fieldValue
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show()
                }
            val imageView: ImageView = findViewById(R.id.info_usersflower_image)
            val folderRef = storageRef.child(email)
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
        }
    }
}