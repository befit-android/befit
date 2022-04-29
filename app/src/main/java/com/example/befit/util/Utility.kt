package com.example.befit.util

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast

@SuppressLint("StaticFieldLeak")
object Utility {

    fun Context.isInternetAvailable(): Boolean {
        try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return if (netInfo != null && netInfo.isConnected) {
                true
            } else {
                Toast.makeText(this, "Интернет недоступен. Попробуйте еще раз", Toast.LENGTH_LONG).show()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private var progressBar: ProgressBar? = null

    // show progressbar
    fun Context.showProgressBar() {
        try {
            val layout =
                (this as? Activity)?.findViewById<View>(android.R.id.content)?.rootView as? ViewGroup
            progressBar = ProgressBar(this, null, R.attr.progressBarStyleLarge)

            progressBar?.let { it1 ->
                it1.isIndeterminate = true

                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )

                val rl = RelativeLayout(this)
                rl.gravity = Gravity.CENTER
                rl.addView(it1)
                layout?.addView(rl, params)
                it1.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // hide progressbar
    fun hideProgressBar() {
        try {
            progressBar?.let {
                it.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}