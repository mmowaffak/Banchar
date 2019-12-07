package com.hackathon.project1


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.project1.data.APIGenerator
import com.hackathon.project1.data.model.*
import com.hackathon.project1.helpers.AppAlerts
import com.hackathon.project1.helpers.AppCache
import com.hackathon.project1.helpers.AppLoading
import com.hackathon.project1.helpers.AppStorage
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHistory()


    }


    private fun setupAdpater(data: List<History>) {
        val adapter = HistoryAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }



    private fun getHistory() {

        AppLoading.show(context)

        val data = RequestData(userId = AppStorage.userId.toString())

        AppLoading.show(context)

        APIGenerator.getClient().getHistory(data, AppStorage.jwtToken).enqueue(object: Callback<List<History>> {

            override fun onResponse(call: Call<List<History>>, response: Response<List<History>>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()
                    Logger.d(data)
                    if (data.isNullOrEmpty()) {
                        recyclerView.visibility = GONE
                        msg.visibility = VISIBLE
                    } else {
                        recyclerView.visibility = VISIBLE
                        msg.visibility = GONE
                        setupAdpater(data)
                    }

                    AppLoading.hide()


                }
            }

            override fun onFailure(call: Call<List<History>>, t: Throwable) {
                Logger.d(t)
                AppLoading.hide()
            }

        })
    }




}
