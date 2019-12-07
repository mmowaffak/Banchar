package com.hackathon.project1


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.hackathon.project1.data.APIGenerator
import com.hackathon.project1.data.model.AuthData
import com.hackathon.project1.data.model.UserData
import com.hackathon.project1.helpers.AppAlerts
import com.hackathon.project1.helpers.AppLoading
import com.hackathon.project1.helpers.AppStorage
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_sign_up_3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SignUp3Fragment : Fragment() {

    var name: String? = null
    var email: String? = null
    var username: String? = null
    var type: Int? = null
    var password: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        name = arguments?.getString("NAME")
        email = arguments?.getString("EMAIL")
        username = arguments?.getString("USERNAME")
        type = arguments?.getInt("TYPE")
        password = arguments?.getString("PASSWORD")

        button.setOnClickListener {
            if (isValid()) {
                val data = AuthData(email,
                    name,
                    username,
                    password,
                    type,
                    inputCareTypeValue.text.toString(),
                    inputLicensePlateValue.text.toString())

                register(data)
            }
        }

        imageView.setOnClickListener {
            uploadImage()
        }


    }

    private fun isValid(): Boolean {

        var status = true

        if (inputCareTypeValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }

        if (inputLicensePlateValue.text.isNullOrEmpty()) {
            AppAlerts.missingInput(context)
            status = false
        }

        return status
    }



    private fun register(data: AuthData) {
        AppLoading.show(context)

        APIGenerator.getClient().signup(data).enqueue(object: Callback<UserData> {

            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    AppStorage.jwtToken = "Bearer "+ data?.accessToken
                    AppStorage.userId = data?.id
                    Logger.d(data)
                    AppLoading.hide()
                    context?.let { HomeActivity.start(it) }
                    activity?.finish()

                } else {
                    AppLoading.hide()
                    val data = response.body()
                    Logger.d(data)
                    AppAlerts.showError(context, "Email Address already in use!")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Logger.d(t)
                AppLoading.hide()
            }

        })
    }

    private fun uploadImage() {
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                imageView.setImageURI(fileUri)

                //You can get File object from intent
                val file:File? = ImagePicker.getFile(data)

                //You can also get File Path from intent
                val filePath:String? = ImagePicker.getFilePath(data)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }





}
