package com.example.manseryeok.page

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.manseryeok.R
import com.example.manseryeok.Utils.BaseActivity
import com.example.manseryeok.Utils.NotionAPI.NotionAPI
import com.example.manseryeok.Utils.NotionAPI.ResponseDTO.*
import com.example.manseryeok.Utils.SecretConstants
import com.example.manseryeok.databinding.ActivityInquiryBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InquiryActivity : BaseActivity() {
    private val notionAPI by lazy { NotionAPI.create() }
    private val binding by lazy { ActivityInquiryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarInquiry)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        binding.run {
            btnInquiryFinish.setOnClickListener {
                sendInquiry()
            }
        }
    }

    private fun sendInquiry() {
        showProgress(this, getString(R.string.glb_please_wait))

        val title = binding.etInquiryTitle.text.toString()
        val content = binding.etInquiryContent.text.toString()

        val requestDTO = InquiryRequestDTO(
            parent = Parent(SecretConstants.NOTION_QUESTION_DB_ID),
            properties = Properties(Title(List(1) { TitleX(Text(title)) })),
            children = List(1) { Children(Paragraph(List(1) { RichText(Text(content)) })) }
        )

        val inquiryCall = notionAPI.registerInquiry(
            notionVersion = NotionAPI.NOTION_API_VERSION,
            token = SecretConstants.NOTION_TOKEN,
            inquiryRequestDTO = requestDTO
        )

        inquiryCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    hideProgress()
                    Toast.makeText(applicationContext,getString(R.string.msg_send_inquiry_complete),Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    hideProgress()
                    Toast.makeText(applicationContext, getString(R.string.msg_send_inquiry_fail), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                hideProgress()
                Toast.makeText(applicationContext, getString(R.string.msg_send_inquiry_fail), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}