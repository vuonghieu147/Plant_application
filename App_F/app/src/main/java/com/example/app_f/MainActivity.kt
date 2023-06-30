package com.example.app_f

import android.content.Context
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        //Khoá xoay màn hình
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            loginHome()
        }
        val signupButton = findViewById<Button>(R.id.signup_button)
        signupButton.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        val forgotButton = findViewById<TextView>(R.id.forgot_pass)
        forgotButton.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        //Show/Hide PASSWORD --> Finish
        val password:EditText = findViewById(R.id.login_pass)
        val showPasswordButton = findViewById<ImageButton>(R.id.show_pass)
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
    private var backButtonCount = 0

    override fun onBackPressed() {
        if (backButtonCount >= 1) {
            finishAffinity() // Kết thúc tất cả Activity và thoát ứng dụng
        } else {
            Toast.makeText(this, "Go back again to exit", Toast.LENGTH_SHORT).show()
            backButtonCount++
        }
    }
    private fun loginHome(){
        val email:EditText = findViewById(R.id.login_user)
        val password:EditText = findViewById(R.id.login_pass)

        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        if (inputEmail.isEmpty() || inputPassword.isEmpty()){
            Toast.makeText(this,"Please fill in EMAIL and PASSWORD",Toast.LENGTH_SHORT)
                .show()
            return
        }


        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val intent = Intent(this,NavHome::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext,
                        "Login failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
            .addOnFailureListener {
                Toast.makeText(baseContext,"Authentication failed. ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }
    }
}