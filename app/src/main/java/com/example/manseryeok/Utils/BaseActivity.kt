package com.example.manseryeok.Utils

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.manseryeok.R

open class BaseActivity : AppCompatActivity() {
    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgress(activity: Activity, text: String) {
        if (progressDialog != null) {
            progressDialog = null
        }

        progressDialog = Dialog(activity)
        progressDialog?.setContentView(R.layout.dialog_progress)
        progressDialog?.findViewById<TextView>(R.id.tv_dialog_progress)?.text = text
        progressDialog?.setCancelable(false) // 다이얼로그 외 터치 시 취소 막음
        progressDialog?.show()
    }

    fun hideProgress() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
        }
        progressDialog = null
    }
}