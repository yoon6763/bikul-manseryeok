package com.bikulwon.manseryeok.service

import com.bikulwon.manseryeok.models.notion.request.AdvertiseRequestDTO
import com.bikulwon.manseryeok.models.notion.response.advertise.AdvertiseResponseDTO
import com.bikulwon.manseryeok.models.notion.response.inquery.InquiryRequestDTO
import com.bikulwon.manseryeok.utils.SecretConstants
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


    @POST("v1/databases/{id}/query")
    fun getAdvertiseInfo(
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
        @Path("id") databaseId: String,
        @Body advertiseRequestDTO: AdvertiseRequestDTO
    ): Call<AdvertiseResponseDTO>


    @GET("v1/databases/{id}/query")
    fun getBusinessInfo(
        @Header("Notion-Version") notionVersion: String = NOTION_API_VERSION,
        @Header("Authorization") token: String = SecretConstants.NOTION_TOKEN,
        @Path("id") databaseId: String = SecretConstants.NOTION_BUSINESS_INFO_DB_ID
    ): Call<ResponseBody>

}