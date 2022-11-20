package projekt.cloud.piece.pic.util

import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object HttpUtil {

    private val okHttpClient = OkHttpClient.Builder()
        .build()

    private const val POST = "POST"
    private const val GET = "GET"

    private val requestBodyType =
        "application/json; charset=UTF-8".toMediaType()

    fun httpGet(url: String,
                headers: Map<String, String> = mapOf(),
                requestBody: String? = null) =
        request(url, GET, headers.toHeaders(), requestBody?.toRequestBody(requestBodyType))

    fun httpPost(url: String,
                headers: Map<String, String> = mapOf(),
                requestBody: String) =
        request(url, POST, headers.toHeaders(), requestBody.toRequestBody(requestBodyType))

    private fun request(url: String,
                        method: String,
                        headers: Headers,
                        requestBody: RequestBody? = null) =
        okHttpClient.newCall(
            Request.Builder()
                .url(url)
                .headers(headers)
                .method(method, requestBody)
                .build()
        ).execute().body.toString()

}