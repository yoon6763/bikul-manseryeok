package com.example.manseryeok.utils.notionAPI

import com.example.manseryeok.utils.notionAPI.responseDTO.InquiryRequestDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NotionAPI {

    companion object {
        private const val baseNotionUri = "https://api.notion.com/"
        const val NOTION_API_VERSION = "2022-02-22"

        fun create(): NotionAPI {
            return Retrofit.Builder()
                .baseUrl(baseNotionUri)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(NotionAPI::class.java)
        }
    }

    // 내용과 함께 페이지 등록
    @POST("v1/pages")
    fun registerInquiry(
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
        @Body inquiryRequestDTO: InquiryRequestDTO
    ): Call<ResponseBody>
}