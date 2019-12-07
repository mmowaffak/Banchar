package com.hackathon.project1

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hackathon.project1.data.APIGenerator
import com.hackathon.project1.data.model.*
import com.hackathon.project1.helpers.AppCache
import com.hackathon.project1.helpers.AppLoading
import com.hackathon.project1.helpers.AppStorage
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DriverActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, DriverActivity::class.java)
            context.startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        getInfo()

        listenToOrder()


    }


    private fun listenToOrder() {


        val data = RequestData(winchId = AppStorage.userId?.toString())

        APIGenerator.getClient().anyRequestsAvailableToService(data, AppStorage.jwtToken).enqueue(object: Callback<OrderData> {

            override fun onResponse(call: Call<OrderData>, response: Response<OrderData>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)

                    if (data?.id == null) {
                        Handler().postDelayed({listenToOrder()}, 5000)
                    } else {
                        AppCache.requestId = data?.id?.toString()
                        textView2.text = "From: "+ data?.customerName
                        textView5.text = "Car type: "+ data?.carType
                        textView6.text = "License plate number: "+data?.licensePlate
                        textView6.text = "License plate number: "+data?.licensePlate
                        textView7.text = "Problem: "+data?.reqDetails
                        orderLayout.visibility = VISIBLE

                        buttonAccept.setOnClickListener {
                            accept()
                        }

                        buttonDecline.setOnClickListener {
                            decline()
                        }



                    }





                }
            }

            override fun onFailure(call: Call<OrderData>, t: Throwable) {
                Logger.d(t)
            }

        })


    }


    private fun getInfo() {

        AppLoading.show(this)

        val data = RequestData(userId = AppStorage.userId?.toString())

        AppLoading.show(this)

        APIGenerator.getClient().getUserInfo(data, AppStorage.jwtToken).enqueue(object: Callback<DriverData> {

            override fun onResponse(call: Call<DriverData>, response: Response<DriverData>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)

                    ratingBar.rating = data?.rating?.toFloat()!!

                    tvPrice.text = "Rating Score based on "+ data?.numberofreviews+" "+"Reviews"

                    AppLoading.hide()




                }
            }

            override fun onFailure(call: Call<DriverData>, t: Throwable) {
                Logger.d(t)
                AppLoading.hide()
            }

        })
    }




    private fun accept() {


        val data = RequestData(reqId = AppCache.requestId)

        AppLoading.show(this)

        APIGenerator.getClient().acceptRequest(data, AppStorage.jwtToken).enqueue(object: Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)
                    orderLayout.visibility = GONE
                    AppLoading.hide()
                    openMapWithBranchDirection()
                    listenToOrder()

                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Logger.d(t)
                AppLoading.hide()
                orderLayout.visibility = GONE
                AppLoading.hide()
                openMapWithBranchDirection()
                listenToOrder()
            }

        })


    }



    private fun decline() {


        val data = RequestData(reqId = AppCache.requestId)

        AppLoading.show(this)

        APIGenerator.getClient().declineRequest(data, AppStorage.jwtToken).enqueue(object: Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)
                    orderLayout.visibility = GONE
                    AppLoading.hide()
                    listenToOrder()


                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Logger.d(t)
                orderLayout.visibility = GONE
                AppLoading.hide()
                listenToOrder()
            }

        })


    }


    private fun openMapWithBranchDirection() {
        val uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", 29.357921,47.907380)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }



}
