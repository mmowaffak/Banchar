package com.hackathon.project1


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hackathon.project1.data.APIGenerator
import com.hackathon.project1.data.model.AuthData
import com.hackathon.project1.data.model.UserData
import com.hackathon.project1.helpers.AppAlerts
import com.hackathon.project1.helpers.AppLoading
import com.hackathon.project1.helpers.AppStorage
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpLabel.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        button.setOnClickListener { if (isValid()) { login() } }

    }


    private fun isValid(): Boolean {

        var status = true

        if (inputPasswordValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }

        if (inputEmailValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }



        return status
    }


    private fun login() {

        val data = AuthData(usernameOrEmail = inputEmailValue.text.toString(), password = inputPasswordValue.text.toString())

        AppLoading.show(context)

        APIGenerator.getClient().login(data).enqueue(object: Callback<UserData> {

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)
                    AppStorage.jwtToken = "Bearer "+ data?.accessToken
                    data?.id?.let { AppStorage.userId = it}
                    AppLoading.hide()

                    if (data?.isDriver!!) {
                        context?.let { DriverActivity.start(it) }
                    } else {
                        context?.let { HomeActivity.start(it) }
                    }

                    activity?.finish()

                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Logger.d(t)
                AppLoading.hide()
            }

        })
    }


//    fun apiCallGet() {
//
//        AppLoading.show(context)
//
//        APIGenerator.getClient().getUser(1).enqueue(object: Callback<Any> {
//
//            override fun onResponse(call: Call<Any>, response: Response<Any>) {
//                if (response.isSuccessful && response.body() != null) {
//                    val data = response.body()
//                    Logger.d(data)
//                    AppLoading.hide()
//                }
//            }
//
//            override fun onFailure(call: Call<Any>, t: Throwable) {
//                Logger.d(t)
//                AppLoading.hide()
//            }
//
//        })
//    }




}
