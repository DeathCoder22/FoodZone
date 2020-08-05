package com.atul.foodzone.fragment

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
 * Use the [AboutAppFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutAppFragment : Fragment() {
    lateinit var recyclerAboutApp : RecyclerView
    lateinit var recyclerAdapter : FaqAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

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
        val view = inflater.inflate(R.layout.fragment_about_app, container, false)

        recyclerAboutApp = view.findViewById(R.id.recyclerAboutApp)
        layoutManager = LinearLayoutManager(activity as Context)
        val AppList = arrayListOf<QuesAns>()

        val obj1 = QuesAns(
            "What does the Application do? ->",
            "The FoodZone app is based on 'How a Food Delivery App works!'.The FoodZone App has a variety of Activities such as registerActivity, ForgotPasswordActivity and many more.Though it does not really sends you food orders or do any type of transaction, it is just project developed in Android Studio"
        )
        val obj2 = QuesAns(
            "About Registering User ->",
            "The FoodZone App registers a user by getting details from the user and then sending to the Application API server and returning a Boolean back to the application stating whether the user was registered or not." +
                    "If some error or exception occurs or the user is already registered, respective methods are used to handle them" +
                    "The user can go to Register Activity by clicking on the SignUp text on the login Screen"
        )
        val obj3 = QuesAns(
            "About ForgotPassword ->",
            "The application has a text on Login Screen saying 'ForgotPasswrd?'. By clicking on this text, the user goes to another screen where he/she is required to" +
                    "enter the registered Mobile Number and Email Id, and on clicking the button on the screen, the user is taken to another screen saying 'Reset Password" +
                    "here, the user needs to check the registered EmailId for an OTP to reset the password." +
                    "                                                " +
                    "Note that only one OTP will be sent in a 24 hours"
        )
        val obj4 = QuesAns(
            "About Login ->",
            "The FoodZone App has a Login Screen which displays after the splash screen." +
                    "Here, the user needs to enter his/her registered mobile number along with the password to log into the application" +
                    "Once logged in, the user need not enter the details again on using the app again, but has to login again if user re-install the app or Logout of the app."
        )
        val obj5 = QuesAns(
            "About All Restaurants ->",
            "This page displays the list of the restaurants available from the server API via internet. User can go to the restaurant menu by simply clicking on the restaurant icons." +
                    "The Restaurants can also be sorted by various means by clicking on the icon on the right top corner on the toolbar."
        )
        val obj6 = QuesAns(
            "About Restaurants Menu ->",
            "This page contains the Menu List or the Food Items offered by respective restaurants. You can add or remove Food Items from your cart here." +
                    "Also you can add a restaurant to favourites by clicking on the heart icon at the top of the screen."
        )
        val obj7 = QuesAns(
            "About My Cart->",
            "Items added to your appear here. User can also view the respective cost and the total cost of each Food Item here. On clicking the Place Order button, a request is sent " +
                    "to the sever API and the result is shown."
        )
        val obj8 = QuesAns(
            "About Favourite Restaurants ->",
            "Restaurants that User adds to favourites appear here. This is done using local database."
        )
        val obj9 = QuesAns(
            "About Profile ->",
            "The details of the user such as Name, Mobile Number, Email Id, etc. appear here. This is done using Shared Preferences"
        )
        val obj10 = QuesAns(
            "About About App ->",
            "The details about the app appear here."
        )
        val obj11 = QuesAns(
            "About FAQs ->",
            "The common questions regarding the app appear here."
        )
        val obj12 = QuesAns(
            "About Logout ->",
            "The user can logout from the app by clicking on this."
        )
        val obj13 = QuesAns(
            "About Creator ->",
            "This Screen shows the details about the creator of the app"
        )
        AppList.add(obj1)
        AppList.add(obj2)
        AppList.add(obj3)
        AppList.add(obj4)
        AppList.add(obj5)
        AppList.add(obj6)
        AppList.add(obj7)
        AppList.add(obj8)
        AppList.add(obj9)
        AppList.add(obj10)
        AppList.add(obj13)
        AppList.add(obj11)
        AppList.add(obj12)

        recyclerAdapter = FaqAdapter(activity as Context,AppList)
        recyclerAboutApp.adapter = recyclerAdapter
        recyclerAboutApp.layoutManager = layoutManager
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutAppFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutAppFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}