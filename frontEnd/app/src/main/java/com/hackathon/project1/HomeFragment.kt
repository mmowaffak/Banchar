package com.hackathon.project1


import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.braintreepayments.cardform.view.CardForm
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.hackathon.project1.data.APIGenerator
import com.hackathon.project1.data.model.DriverData
import com.hackathon.project1.data.model.PayRateData
import com.hackathon.project1.data.model.RequestData
import com.hackathon.project1.helpers.AppAlerts
import com.hackathon.project1.helpers.AppCache
import com.hackathon.project1.helpers.AppLoading
import com.hackathon.project1.helpers.AppStorage
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.title_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class HomeFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var winch: Marker? = null
    private var user: Marker? = null

    private var isCancel = false
    private var stopCheck = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        rightArrow.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }

//        getFinalData()


//        tv.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable) {}
//
//            override fun beforeTextChanged(s: CharSequence, start: Int,
//                                           count: Int, after: Int) {
//
//
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int,
//                                       before: Int, count: Int) {
//
//                button.isEnabled = s.isNotEmpty()
//            }
//        })






        button.setOnClickListener {
            if (isCancel) {
                cancelRequest()
            } else {
                request()
            }

        }


    }



    private fun request() {

        loading.visibility = VISIBLE

//        AppLoading.show(context)
//
        button.text = "Cancel Request"
        textInputLayout2.visibility = GONE
        isCancel = true

        val data = RequestData(id = AppStorage.userId, details = tv.text.toString())
        APIGenerator.getClient().request(data, AppStorage.jwtToken).enqueue(object: Callback<Int> {

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)
                    AppCache.requestId = data?.toString()

                    requestStatus()



                } else {
//                    AppLoading.hide()
//                    val data = response.body()
//                    Logger.d(data)
//                    AppAlerts.showError(context, "Email Address already in use!")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Logger.d(t)
                loading.visibility = GONE
            }

        })
    }


    private fun requestStatus() {


        val data = RequestData(reqId = AppCache.requestId)
        APIGenerator.getClient().requestStatus(data, AppStorage.jwtToken).enqueue(object: Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)
                    if (data == true) {
                        whoIsMyWinch()
                    } else if (!stopCheck) {
                        Handler().postDelayed({ requestStatus() }, 3000)

                    } else if (stopCheck) {
                        stopCheck = false
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Logger.d(t)
                loading.visibility = GONE
            }

        })
    }


    private fun whoIsMyWinch() {


        val data = RequestData(reqId = AppCache.requestId)
        APIGenerator.getClient().whoIsMyWinch(data, AppStorage.jwtToken).enqueue(object: Callback<DriverData> {

            override fun onResponse(call: Call<DriverData>, response: Response<DriverData>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    actionLayout.visibility = GONE
                    AppCache.winchId = data?.id?.toString()
                    data?.let { addMarkers(it) }
                    Logger.d(data)
                    loading.visibility = GONE

                    Handler().postDelayed({getCurrentLocation()}, 1000)


                }
            }

            override fun onFailure(call: Call<DriverData>, t: Throwable) {
                Logger.d(t)
                loading.visibility = GONE
            }

        })
    }


    private fun getCurrentLocation() {


        val data = RequestData( winchId =  AppCache.winchId)
        APIGenerator.getClient().whereIsMyWinch(data, AppStorage.jwtToken).enqueue(object: Callback<DriverData> {

            override fun onResponse(call: Call<DriverData>, response: Response<DriverData>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    data?.let {
                        winch?.remove()
                        user?.remove()
                        addMarkers(it)

                    }
//                    val value = distance(29.357921,47.907380, data?.lat?.toDouble()!!, data?.lon?.toDouble()!!)
                    if (data?.lat?.toDouble()!! == 29.357921 && data.lon?.toDouble() == 47.907380 ) {
                        AppAlerts.showError(context,"Banchar winch has arrived :)")
                        isTripComplete()
                    } else {
                        Handler().postDelayed({getCurrentLocation()}, 5000)
                    }
                    Logger.d(data)


                }
            }

            override fun onFailure(call: Call<DriverData>, t: Throwable) {
                Logger.d(t)
            }

        })
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        mMap?.uiSettings?.isZoomControlsEnabled = true

        mMap?.isMyLocationEnabled = true

        val latLngDestination = LatLng(29.357921,47.907380) // User Location
//        user =  mMap?.addMarker(MarkerOptions().position(latLngDestination).title("My Location"))
//        user?.showInfoWindow()
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngDestination, 17.0f))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
        mMap?.uiSettings?.isZoomControlsEnabled = true


    }


    fun addMarkers(data: DriverData) {
        val latD = data.lat?.toDouble()
        val lonD = data.lon?.toDouble()
        val latLngOrigin = LatLng(latD!!,lonD!!) // Driver
        val latLngDestination = LatLng(29.357921,47.907380) // User Location
        winch = mMap?.addMarker(latLngOrigin.let { MarkerOptions().position(it).title("Banchar") })
//        user =  mMap?.addMarker(MarkerOptions().position(latLngDestination).title("My Location"))
        winch?.showInfoWindow()
//        user?.showInfoWindow()
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 17.0f))

    }


    private fun cancelRequest() {

        loading.visibility = GONE

        actionLayout.visibility = GONE

        AppLoading.show(context)

        val data = RequestData(reqId = AppCache.requestId)
        APIGenerator.getClient().cancelRequest(data, AppStorage.jwtToken).enqueue(object: Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    user?.remove()
                    winch?.remove()
                    tv.text = null
                    button.text = "Request Now"
                    textInputLayout2.visibility = VISIBLE
                    actionLayout.visibility = VISIBLE
                    Logger.d(data)
                    isCancel = false
                    stopCheck = true
                    AppLoading.hide()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Logger.d(t)
                user?.remove()
                winch?.remove()
                tv.text = null
                button.text = "Request Now"
                textInputLayout2.visibility = VISIBLE
                actionLayout.visibility = VISIBLE
                Logger.d(data)
                isCancel = false
                stopCheck = true
                AppLoading.hide()
            }

        })
    }



    fun isTripComplete() {
        val data = RequestData(reqId = AppCache.requestId)
        APIGenerator.getClient().isMyTripComplete(data, AppStorage.jwtToken).enqueue(object: Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    if (data == true) {
                        getFinalData()
                        winch?.remove()
                        tv.text = null
                        button.text = "Request Now"
                        textInputLayout2.visibility = VISIBLE
                        actionLayout.visibility = VISIBLE
                        Logger.d(data)
                    } else {
                        Handler().postDelayed({isTripComplete()}, 5000)
                    }
                    Logger.d(data)


                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Logger.d(t)
            }

        })
    }


    fun getFinalData() {
        val dialog = context?.let {
            MaterialDialog(it)
                .customView(R.layout.my_custom_view, scrollable = true)
        }

        val customView = dialog?.getCustomView()
        val ratingBar = customView?.findViewById<RatingBar>(R.id.ratingBar)
        val rateValue = customView?.findViewById<TextInputEditText>(R.id.rateValue)
        val buttonCash = customView?.findViewById<Button>(R.id.buttonCash)
        val buttonCard = customView?.findViewById<Button>(R.id.buttonCard)

        buttonCard?.setOnClickListener {
            Logger.d(ratingBar?.rating)
            dialog?.hide()
            val rateData = PayRateData(AppCache.requestId,rateValue?.text?.toString(), "2", ratingBar?.rating?.toString() )
            openKnet(rateData)
        }

        buttonCash?.setOnClickListener {
            val rateData = PayRateData(AppCache.requestId,rateValue?.text?.toString(), "1", ratingBar?.rating?.toString() )
            APIGenerator.getClient().ratePayWinch(rateData, AppStorage.jwtToken).enqueue(object: Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {}
                override fun onFailure(call: Call<Any>, t: Throwable) {}
            })

            dialog?.hide()
            AppAlerts.showError(context, "Thank you for using our service")
        }

        dialog?.cancelable(false)

        dialog?.show()
    }



    fun openKnet(rateData:PayRateData) {

        try {
            APIGenerator.getClient().ratePayWinch(rateData, AppStorage.jwtToken).enqueue(object: Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {}
                override fun onFailure(call: Call<Any>, t: Throwable) {}
            })
        } catch (e: Exception) {}

        context?.let { PayActivity.start(it) }




//        dialog?.hide()
//        val dialog = context?.let {
//            MaterialDialog(it)
//                .customView(R.layout.payemnt_gate_way, scrollable = true)
//        }
//
//        val customView = dialog?.getCustomView()
////        val buttonCard = customView?.findViewById<Button>(R.id.buttonCash)
//
////
////        buttonCard?.setOnClickListener {

////            AppAlerts.showError(context, "Thank you for using our service")
//        }
//
//
//        dialog?.cancelable(false)
//
//        dialog?.show()
    }


}
