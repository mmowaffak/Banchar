package com.hackathon.project1


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hackathon.project1.helpers.AppAlerts
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    var isVehicle = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            isVehicle = isChecked
        }

        button.setOnClickListener {

            if (isValid()) {
                view.findNavController().navigate(R.id.action_signUpFragment_to_signUp2Fragment, getInputs())
            }
        }

    }


    private fun isValid(): Boolean {

        var status = true

        if (inputNameVale.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }
        if (inputEmailValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }
        if (inputUsernameValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }

        return status
    }



    private fun getInputs(): Bundle {

        val bundle = Bundle()

        bundle.putString("NAME", inputNameVale.text.toString())
        bundle.putString("EMAIL", inputEmailValue.text.toString())
        bundle.putString("USERNAME", inputUsernameValue.text.toString())
        bundle.putInt("TYPE", if (isVehicle) 1 else 2)

        return bundle
    }




}
