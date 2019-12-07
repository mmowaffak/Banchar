package com.hackathon.project1


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hackathon.project1.helpers.AppAlerts
import kotlinx.android.synthetic.main.fragment_sign_up_2.*

class SignUp2Fragment : Fragment() {

    var name: String? = null
    var email: String? = null
    var username: String? = null
    var type: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        name = arguments?.getString("NAME")
        email = arguments?.getString("EMAIL")
        username = arguments?.getString("USERNAME")
        type = arguments?.getInt("TYPE")

        button.setOnClickListener {
            if (isValid()) {
                view.findNavController().navigate(R.id.action_signUp2Fragment_to_signUp3Fragment, getInputs())
            }

        }


    }


    private fun isValid(): Boolean {

        var status = true

        if (inputPasswordValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }

        if (inputPasswordConfValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }

        if (inputPasswordConfValue.text?.toString() != inputPasswordValue.text?.toString()) {
            AppAlerts.passwordNotMatchInput(context)
            status = false
        }


        return status
    }



    private fun getInputs(): Bundle {

        val bundle = Bundle()

        bundle.putString("NAME", name)
        bundle.putString("EMAIL",email)
        bundle.putString("USERNAME", username)
        type?.let { bundle.putInt("TYPE", it) }
        bundle.putString("PASSWORD", inputPasswordValue.text.toString())

        return bundle
    }




}
