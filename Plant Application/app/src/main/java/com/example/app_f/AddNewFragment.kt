package com.example.app_f


import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storageRef = FirebaseStorage.getInstance().reference

        val View = inflater.inflate(R.layout.fragment_add_new, container, false)
        val capturedPhoto = arguments?.getParcelable<Bitmap>("capturedPhoto")
        val saveButton = View.findViewById<Button>(R.id.add_save_button)
        val currentUser = auth.currentUser
        val email = currentUser?.email

        saveButton.setOnClickListener {
            val name = View.findViewById<EditText>(R.id.add_name_flower).text.toString()
            val description = View.findViewById<EditText>(R.id.add_description_flower).text.toString()
            val imageRef = storageRef.child("$email/$name.jpg")

            val baos = ByteArrayOutputStream()
            capturedPhoto?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = imageRef.putBytes(data)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Upload ảnh thành công
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    // Lưu thông tin ảnh và imageUrl vào Cloud Firestore
                    val photoInfo = hashMapOf(
                        "imageUrl" to imageUrl,
                        "description" to description
                    )

                    if (email != null) {
                        db.collection(email).document("User's Flower").collection("species").document(name)
                            .set(photoInfo, SetOptions.merge())
                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_layout, HomeFragment())
                        fragmentTransaction.commit()
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error uploading photo: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        return View
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddNewFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val REQUEST_CAMERA_PERMISSION = 1001
        private const val REQUEST_IMAGE_CAPTURE = 1002
        private lateinit var auth: FirebaseAuth
        fun newInstance(param1: String, param2: String) =
            AddNewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}