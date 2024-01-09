package com.example.manseryeok.service.compass

import com.example.manseryeok.models.naversearch.NaverSearchResult
import com.example.manseryeok.models.notion.request.AdvertiseRequestDTO
import com.example.manseryeok.models.notion.response.advertise.AdvertiseResponseDTO
import com.example.manseryeok.models.notion.response.inquery.InquiryRequestDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NaverSearchAPI {

    companion object {
        private const val BASE_URL = "https://openapi.naver.com"

        fun create(): NaverSearchAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NaverSearchAPI::class.java)
        }
    }

    @GET("/v1/search/local.json")
    fun getSearchResult(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int = 5,
    ): Call<NaverSearchResult>


}