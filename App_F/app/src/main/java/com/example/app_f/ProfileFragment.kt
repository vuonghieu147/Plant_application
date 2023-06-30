package com.example.app_f

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.profile, container, false)

        //Lấy tên người dùng
        val currentUser = auth.currentUser
        val displayName = currentUser?.displayName
        val nameUser = view.findViewById<TextView>(R.id.user_name)
        nameUser.text = "Name: $displayName"

        //Lấy Email người dùng
        val email = currentUser?.email
        val emailUser = view.findViewById<TextView>(R.id.profile_email)
        emailUser.text = "Email: $email"

        val buttonLogout = view.findViewById<Button>(R.id.logout_button)
        buttonLogout.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        val speciesLiked = view.findViewById<Button>(R.id.profile_species_button)
        speciesLiked.setOnClickListener {
            val intent = Intent(requireContext(),SpeciesLiked::class.java)
            startActivity(intent)
        }
        val usersFlower = view.findViewById<Button>(R.id.profile_userflower_button)
        usersFlower.setOnClickListener {
            val intent = Intent(requireContext(),UsersFlower::class.java)
            startActivity(intent)
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}