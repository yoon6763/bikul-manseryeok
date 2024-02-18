package com.bikulwon.manseryeok.service.compass

import com.bikulwon.manseryeok.models.address.AddressSearchDto
import com.bikulwon.manseryeok.utils.SecretConstants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AddressSearchAPI {

    companion object {
        private const val BASE_URL = "https://business.juso.go.kr"

        fun create(): AddressSearchAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AddressSearchAPI::class.java)
        }
    }

    @GET("/addrlink/addrLinkApi.do")
    fun getSearchResult(
        @Query("confmKey") confmKey: String = SecretConstants.ADDRESS_SEARCH_CONFMKEY,
        @Query("currentPage") currentPage: Int = 1,
        @Query("countPerPage") countPerPage: Int = 10,
        @Query("keyword") keyword: String,
        @Query("resultType") resultType: String = "json",
    ): Call<AddressSearchDto>
}