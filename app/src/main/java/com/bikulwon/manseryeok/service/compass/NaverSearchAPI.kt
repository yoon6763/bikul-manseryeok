package com.bikulwon.manseryeok.service.compass

import com.bikulwon.manseryeok.models.naversearch.NaverSearchResult
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