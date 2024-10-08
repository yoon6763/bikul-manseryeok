package com.bikulwon.manseryeok.page

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import com.bikulwon.manseryeok.R
import com.bikulwon.manseryeok.service.NotionAPI
import com.bikulwon.manseryeok.utils.SecretConstants
import com.bikulwon.manseryeok.databinding.ActivityInquiryBinding
import com.bikulwon.manseryeok.databinding.DialogInqueryIncludeUserInfoBinding
import com.bikulwon.manseryeok.databinding.DialogInqueryNotIncludeUserInfoBinding
import com.bikulwon.manseryeok.models.notion.response.inquery.Children
import com.bikulwon.manseryeok.models.notion.response.inquery.Email
import com.bikulwon.manseryeok.models.notion.response.inquery.InquiryRequestDTO
import com.bikulwon.manseryeok.models.notion.response.inquery.Paragraph
import com.bikulwon.manseryeok.models.notion.response.inquery.Parent
import com.bikulwon.manseryeok.models.notion.response.inquery.Phone
import com.bikulwon.manseryeok.models.notion.response.inquery.Properties
import com.bikulwon.manseryeok.models.notion.response.inquery.RichText
import com.bikulwon.manseryeok.models.notion.response.inquery.Text
import com.bikulwon.manseryeok.models.notion.response.inquery.Title
import com.bikulwon.manseryeok.models.notion.response.inquery.TitleX
import com.bikulwon.manseryeok.utils.ParentActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InquiryActivity : ParentActivity() {
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
                if (isExistUserInfo()) {
                    showInquiryIncludeUserInfo()
                } else {
                    showInquiryNotIncludeUserInfo()
                }
            }
        }
    }

    private fun showInquiryIncludeUserInfo() {
        val binding = DialogInqueryIncludeUserInfoBinding.inflate(layoutInflater)

        Dialog(this).apply {
            setContentView(binding.root)
            setCanceledOnTouchOutside(false)
            setCancelable(false)

            binding.btnCancel.setOnClickListener { dismiss() }
            binding.btnSend.setOnClickListener {
                if (!binding.cbAgree.isChecked) {
                    showShortToast("개인정보 수집 및 이용에 동의해주세요.")
                    return@setOnClickListener
                }
                sendInquiry()
            }
        }.show()
    }

    private fun showInquiryNotIncludeUserInfo() {
        val binding = DialogInqueryNotIncludeUserInfoBinding.inflate(layoutInflater)

        Dialog(this).apply {
            setContentView(binding.root)
            setCanceledOnTouchOutside(false)
            setCancelable(false)

            binding.btnCancel.setOnClickListener { dismiss() }
            binding.btnSend.setOnClickListener { sendInquiry() }
        }.show()

    }

    private fun isExistUserInfo() = binding.etInquiryEmail.text.toString().isNotEmpty() ||
            binding.etInquiryPhone.text.toString().isNotEmpty()

    private fun sendInquiry() {
        showProgress(this, getString(R.string.glb_please_wait))

        val title = binding.etInquiryTitle.text.toString()
        val content = binding.etInquiryContent.text.toString()

        val requestDTO = InquiryRequestDTO(
            parent = Parent(SecretConstants.NOTION_QUESTION_DB_ID),
            properties = Properties(
                Title = Title(List(1) { TitleX(Text(title)) }),
                Email = Email(binding.etInquiryEmail.text.toString().ifEmpty { " " }),
                Phone = Phone(binding.etInquiryPhone.text.toString().ifEmpty { " " })
            ),
            children = List(1) { Children(Paragraph(List(1) { RichText(Text(content)) })) }
        )

        val inquiryCall = notionAPI.registerInquiry(
            notionVersion = NotionAPI.NOTION_API_VERSION,
            token = SecretConstants.NOTION_TOKEN,
            inquiryRequestDTO = requestDTO
        )

        inquiryCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    hideProgress()
                    showShortToast(R.string.msg_send_inquiry_complete)
                    finish()
                } else {
                    hideProgress()
                    showShortToast(R.string.msg_send_inquiry_fail)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                hideProgress()
                showShortToast(R.string.msg_send_inquiry_fail)
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