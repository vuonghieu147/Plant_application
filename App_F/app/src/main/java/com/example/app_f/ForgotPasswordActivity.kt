package com.example.app_f

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)

        //Khoá xoay màn hình
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val cancelButton = findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            resetPassword()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun resetPassword(){
        val email: EditText = findViewById(R.id.forgot_mail)
        val inputEmail = email.text.toString()
        if (inputEmail.isEmpty()){
            Toast.makeText(this,"Vui lòng điền EMAIL", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val emailRegex = Regex(pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!email.text.matches(emailRegex)) {
            Toast.makeText(this, "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(inputEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent to reset your password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to reset password. Please try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }
}