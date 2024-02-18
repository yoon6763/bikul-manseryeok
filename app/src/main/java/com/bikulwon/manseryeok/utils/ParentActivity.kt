package com.bikulwon.manseryeok.utils

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.bikulwon.manseryeok.R

open class ParentActivity : AppCompatActivity() {
    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
    }

    // text 매개변수가 없으면 디폴트 메시지 출력
    fun showProgress(activity: Activity) {
        if (progressDialog != null) progressDialog = null

        progressDialog = Dialog(activity)
        progressDialog?.setContentView(R.layout.dialog_progress)
        progressDialog?.findViewById<TextView>(R.id.tv_dialog_progress)?.text = "잠시만 기다려주세요"
        progressDialog?.setCancelable(false) // 다이얼로그 외 터치 시 취소 막음
        progressDialog?.show()
    }

    fun showProgress(activity: Activity, text: String) {
        if (progressDialog != null) progressDialog = null

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

    fun showShortToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun showShortToast(resId: Int) {
        Toast.makeText(applicationContext, getString(resId), Toast.LENGTH_SHORT).show()
    }
}