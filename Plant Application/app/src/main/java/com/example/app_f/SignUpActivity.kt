package com.example.app_f

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Khoá xoay màn hình
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Initialize Firebase Auth
        auth = Firebase.auth

        val buttonLogin: TextView = findViewById(R.id.buttontext_login)
        buttonLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val signUpButton = findViewById<Button>(R.id.button_in_signup)
        signUpButton.setOnClickListener{
            signUp()
        }

        //Show/Hide PASSWORD --> Finish
        val password = findViewById<EditText>(R.id.signup_password)
        val showPasswordButton = findViewById<ImageButton>(R.id.imageButton)
        showPasswordButton.setOnClickListener {
            if (password.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // Show password
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showPasswordButton.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                // Hide password
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                showPasswordButton.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }


    }
    //Quay lai LOGIN
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUp(){
        val email = findViewById<EditText>(R.id.signup_email)
        val password = findViewById<EditText>(R.id.signup_password)
        val firstName = findViewById<EditText>(R.id.firstname_signup)
        val lastName = findViewById<EditText>(R.id.lastName_signup)

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()
        val inputFirstName = firstName.text.toString()
        val inputLastName = lastName.text.toString()

        val fullName = "$inputFirstName $inputLastName"


        if (inputEmail.isEmpty() || inputPassword.isEmpty()){
            Toast.makeText(this, "Please fill in EMAIL and PASSWORD",Toast.LENGTH_SHORT)
                .show()
            return
        }





        //Check Mail --> Finish
        val emailRegex = Regex(pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!email.text.matches(emailRegex)) {
            Toast.makeText(this, "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        //Check PASSWORD --> Finish
        val upperCase = Regex("[A-Z]")
        val lowerCase = Regex("[a-z]")
        val digitRegex = Regex("[0-9]")
        val specialChars = Regex("[!@#\$%^&*()_\\-+=\\\\|\\[\\]{}';:/?.>,<]")

        val containsLowerCase = inputPassword.contains(lowerCase)
        val containsUpperCase = inputPassword.contains(upperCase)
        val containsDigit = inputPassword.contains(digitRegex)
        val containsSpecialChar = inputPassword.contains(specialChars)

        if (inputPassword.length < 11 || !containsLowerCase || !containsUpperCase || !containsDigit ||!containsSpecialChar) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 11 ký tự bao gồm ít nhất một chữ cái, số và kí tự đặc biệt, viết thường và viết hoa", Toast.LENGTH_SHORT).show()
            return
        }



        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val firebaseUser = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                    firebaseUser?.updateProfile(profileUpdates)
                    val db = FirebaseFirestore.getInstance()
                    val collectionRef = db.collection(inputEmail)
                    val speciesData = HashMap<String, Any>()
                    collectionRef.document("species").set(speciesData)
                    collectionRef.document("articles").set(speciesData)
                    collectionRef.document("Species Liked").set(mapOf("Species Liked" to listOf<String>()))
                    collectionRef.document("Articles Liked").set(mapOf("Articles Liked" to listOf<String>()))
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        baseContext,
                        "SignUp Success",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "SignUp failed",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error occurred ${it.localizedMessage}",Toast.LENGTH_SHORT)
                    .show()
            }
    }
}