package com.example.befit.util

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.befit.databinding.FragmentRegistrationBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("StaticFieldLeak")
object Utility {

    // check internet connection
    fun Context.isInternetAvailable(): Boolean {
        try {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return if (netInfo != null && netInfo.isConnected) {
                true
            } else {
                Toast.makeText(this, "Интернет недоступен. Попробуйте еще раз", Toast.LENGTH_LONG)
                    .show()
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
                (this as? Activity)?.findViewById<View>(R.id.content)?.rootView as? ViewGroup
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

    // hide keyboard
    fun Context.hideKeyboard(binding: FragmentRegistrationBinding) {
        val inputMethodManager =
            ContextCompat.getSystemService(this, InputMethodManager::class.java)
        if (inputMethodManager!!.isActive)
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    val date: MutableLiveData<Array<String>> = MutableLiveData()

    // show date picker dialog
    fun Context.setDate() {
        val formatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")
        val formatUser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatServer: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, com.example.befit.R.style.DialogTheme,
            { _, mYear, mMonth, mDay ->
                val dateToUser = LocalDate.parse("$mDay.${mMonth + 1}.$mYear", formatDate)
                    .format(formatUser).toString()
                val dateToServer = LocalDate.parse("$mDay.${mMonth + 1}.$mYear", formatDate)
                    .format(formatServer).toString()
                date.value = arrayOf(dateToUser, dateToServer)
            }, year, month, day
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(com.example.befit.R.color.purple_700))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(com.example.befit.R.color.teal_700))
    }
}