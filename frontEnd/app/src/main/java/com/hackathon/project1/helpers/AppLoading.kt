package com.hackathon.project1.helpers

import android.app.Dialog
import android.content.Context
import com.hackathon.project1.R

object AppLoading {

    private var loadingDialog: Dialog? = null

    fun show(context: Context?) {

        if (loadingDialog != null && loadingDialog?.isShowing!!) return

        context?.let {
            loadingDialog = Dialog(it, android.R.style.Theme_Translucent_NoTitleBar)
        }

        loadingDialog?.setContentView(
            R.layout.dialog_loading
        )
        loadingDialog?.setCancelable(false)


        loadingDialog?.show()


    }

    fun hide() {
        try {
            loadingDialog?.let {
                if (it.isShowing) it.dismiss()
                loadingDialog = null
            }
        } catch (e:Exception){}
    }


}