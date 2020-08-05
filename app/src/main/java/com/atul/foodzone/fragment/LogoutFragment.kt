package com.atul.foodzone.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.atul.foodzone.R
import com.atul.foodzone.activity.HomeActivity
import com.atul.foodzone.activity.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogoutFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_logout, container, false)

        sharedPreferences = (activity as FragmentActivity).getSharedPreferences(getString(R.string.preference_name),Context.MODE_PRIVATE)

        val dialog = AlertDialog.Builder(activity as Context)
        dialog.setTitle("Logout!")
        dialog.setMessage("Do you want to Logout ? ")
        dialog.setPositiveButton("Logout"){text,Listener->
            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
            val intent = Intent(activity as Context,LoginActivity::class.java)
            startActivity(intent)
            ActivityCompat.finishAffinity(activity as Activity)
        }
        dialog.setNegativeButton("Cancel"){text,Listener->
           val intent = Intent(activity as Context,HomeActivity::class.java)
            startActivity(intent)
            ActivityCompat.finishAffinity(activity as Activity)
        }
        dialog.create()
        dialog.show()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}