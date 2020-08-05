package com.atul.foodzone.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.atul.foodzone.R

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
    lateinit var txtUsername : TextView
    lateinit var txtEmail : TextView
    lateinit var txtMob : TextView
    lateinit var txtAddress : TextView
    lateinit var sharedPreferences: SharedPreferences

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferences = (activity as FragmentActivity).getSharedPreferences(getString(R.string.preference_name),Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name","UserName").toString()
        val userMobile = "+91-${sharedPreferences.getString("user_mobile_number","0000000000").toString()}"
        val userEmail = sharedPreferences.getString("user_email","Defaultmail@default.com").toString()
        val userAddress = sharedPreferences.getString("user_address","Default Address").toString()

        txtUsername = view.findViewById(R.id.txtUsername)
        txtMob = view.findViewById(R.id.txtMob)
        txtAddress = view.findViewById(R.id.txtAddress)
        txtEmail = view.findViewById(R.id.txtEmail)

        txtUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name,0,0,0)
        txtMob.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mobile,0,0,0)
        txtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email,0,0,0)
        txtAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delivery,0,0,0)

        txtUsername.text = userName
        txtMob.text = userMobile
        txtEmail.text = userEmail
        txtAddress.text = userAddress

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