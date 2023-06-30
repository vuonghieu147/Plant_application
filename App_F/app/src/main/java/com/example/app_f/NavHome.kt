package com.example.app_f

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavHome : AppCompatActivity() {
    private val REQUEST_CAMERA_PERMISSION = 1001
    private val REQUEST_IMAGE_CAPTURE = 1002
    private var tempPhoto: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_home)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val navigationView = findViewById<BottomNavigationView>(R.id.end_navigation)
        replaceFragment(homeFragment)

        navigationView.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.action_home -> replaceFragment(homeFragment)
                R.id.action_add -> requestCameraPermission()
                R.id.action_profile -> replaceFragment(profileFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout,fragment)
            transaction.commit()
        }
    }
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                    val addNewFragment = AddNewFragment()
                    replaceFragment(addNewFragment)
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val capturedPhoto = data?.extras?.get("data") as Bitmap
            val addNewFragment = AddNewFragment()
            val bundle = Bundle()
            bundle.putParcelable("capturedPhoto", capturedPhoto)
            addNewFragment.arguments = bundle
            replaceFragment(addNewFragment)
        }
    }
}
