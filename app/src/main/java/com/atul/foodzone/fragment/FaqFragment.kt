package com.atul.foodzone.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atul.foodzone.R
import com.atul.foodzone.adapter.FaqAdapter
import com.atul.foodzone.model.QuesAns

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqFragment : Fragment() {
    lateinit var recyclerFaq : RecyclerView
    lateinit var recyclerAdapter : FaqAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager

    var quesAnsList = arrayListOf<QuesAns>()

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
        val view = inflater.inflate(R.layout.fragment_faq, container, false)
        recyclerFaq = view.findViewById(R.id.recyclerFaq)
            val obj1 = QuesAns(
                "How Does the app fetches the list of the restaurants? ",
                "The app fetches the list of restaurants from the internet through an API via JSON Requests."
            )
        val obj2 = QuesAns(
        "Is the List of Restaurants shown in the main screen a ScrollView? ",
        "No, The List shown is made with the help of recycler view to minimise code and mistakes and use less resources."
        )
        val obj3 = QuesAns(
            "If I delete the app and install it again, Will I still be able to Login or Do I have to register again? ",
            "No, you don't have to register again, since the app sends the user details to the server via API and thus, your profile is made in our server. So, you can login with your details even if your re-install the app."
        )
        val obj4 = QuesAns(
            "Does the app has its own Database to store information? ",
            "Yes, the app has its own local database and stores vital infrmation of your choices there."
        )
        val obj5 = QuesAns(
            "Does the app shares its users information and activities with external resources? ",
            "No, the app does not shares the user's information or its activities with any kind of third parties. it does some of the user choice information to make the app more user-friendly"
        )
        val obj6 = QuesAns(
            "Does the app requires some private permissions? ",
            "No, the app doesn't need any additional permissions rather than Internet permission."
        )
        val obj7 = QuesAns(
            "Does the uses real money to place orders? ",
            "No, it just a simple working project on working of a Food Delivery App. So, neither you need to pay any money, nor will you get any food ;)"
        )
        val obj8 = QuesAns(
            "Does the app need a really high speed of internet to work? ",
            "No, the app does not a really high speed internet connectivity, but it do need a moderate internet connectivity"
        )

        val obj9 = QuesAns(
            "What languanges have been used in this making of this application? ",
            "The Application Developer has used XML languange for developing the UI or the Front-End of the application and " +
                    "Kotlin languange for making Back-End functionality of the application."
        )
        val obj10 = QuesAns(
            "How does the application sends the requests to the server? ",
            "The application has been made to send GET and POST requests to the server via JSON REQUEST METHODS to fetch necesaary data."
        )
        val obj11 = QuesAns(
            "What all views or layouts have been used in the UI this application? ",
            "Almost every view and layout has been used in this application. Some of the views used are RECYCLER VIEW, IMAGE VIEW, TEXT VIEW, EDIT TEXT,etc. The layouts used are SCROLL VIEW, RELATIVE LAYOUT, LINEAR LAYOUT, " +
                    "etc. Some other widgets were also used namely TOOLBAR, PROGRESSBAR, BUTTON, IMAGE BUTTON, etc."
        )

        quesAnsList.add(obj1)
        quesAnsList.add(obj2)
        quesAnsList.add(obj3)
        quesAnsList.add(obj9)
        quesAnsList.add(obj10)
        quesAnsList.add(obj11)
        quesAnsList.add(obj4)
        quesAnsList.add(obj5)
        quesAnsList.add(obj6)
        quesAnsList.add(obj7)
        quesAnsList.add(obj8)

        layoutManager = LinearLayoutManager(activity as Activity)
        recyclerAdapter = FaqAdapter(activity as Context,quesAnsList)
        recyclerFaq.adapter = recyclerAdapter
        recyclerFaq.layoutManager = layoutManager

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaqFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaqFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}