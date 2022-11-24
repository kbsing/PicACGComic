package projekt.cloud.piece.pic.util

import android.util.Log
import java.net.URLEncoder
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

    const val POST = "POST"
    const val GET = "GET"

    const val RESPONSE_CODE_SUCCESS = 200
    const val RESPONSE_CODE_BAD_REQUEST = 400

    val Map<String, String>.asParams
        get() = StringBuilder().also { stringBuilder ->
            if (this.isNotEmpty()) {
                stringBuilder.append('?')
                forEach { (key, value) ->
                    stringBuilder.append(URLEncoder.encode(key, Charsets.UTF_8.name()))
                        .append('=')
                        .append(URLEncoder.encode(value, Charsets.UTF_8.name()))
                }
            }
        }.toString()

    private val requestBodyType =
        "application/json; charset=UTF-8".toMediaType()

    fun httpGet(url: String, headers: Map<String, String> = mapOf()) =
        request(url, GET, headers.toHeaders())

    fun httpPost(url: String,
                headers: Map<String, String> = mapOf(),
                requestBody: String) =
        request(url, POST, headers.toHeaders(), requestBody.toRequestBody(requestBodyType))

    private fun request(url: String,
                        method: String,
                        headers: Headers,
                        requestBody: RequestBody? = null
    ) = try {
        okHttpClient.newCall(
            Request.Builder()
                .url(url)
                .headers(headers)
                .method(method, requestBody)
                .build()
        ).execute()
    } catch (e: Exception) {
        Log.e("HttpUtil.request", "url=${url} method=$method e:$e")
        null
    }

}