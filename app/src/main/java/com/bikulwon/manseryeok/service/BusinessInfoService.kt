package com.bikulwon.manseryeok.service

import android.content.Context
import com.bikulwon.manseryeok.utils.SecretConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class BusinessInfoService {
    private val notionAPI by lazy { NotionAPI.create() }
    private var initialized = false
    var content: String = ""
        get() {
            if (!initialized) {
                fetchData()
                initialized = true
            }
            return field
        }
        private set

    fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            val awaitResponse = notionAPI.getBusinessInfo(
                notionVersion = NotionAPI.NOTION_API_VERSION,
                token = SecretConstants.NOTION_TOKEN,
                databaseId = SecretConstants.NOTION_BUSINESS_INFO_DB_ID
            ).awaitResponse()

            val tvBusinessInfoContent = StringBuilder()
            awaitResponse.body()?.results?.sortedBy { it.properties.priority.number }
                ?.forEach { result ->
                    tvBusinessInfoContent.append(result.properties.label.title[0].text.content)
                        .append(" : ")
                        .append(result.properties.content.rich_text[0].text.content)
                        .append("\n")
                }

            // 데이터 로드 후 TextView 내용 설정
            content = if (tvBusinessInfoContent.isNotEmpty()) {
                tvBusinessInfoContent.toString()
            } else {
                "정보가 없습니다."
            }

            initialized = true
        }
    }

    fun isInitialized(): Boolean {
        return initialized
    }

}