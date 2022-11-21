package projekt.cloud.piece.pic.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import projekt.cloud.piece.pic.api.PicApi.API_SIGN_IN
import projekt.cloud.piece.pic.api.PicApi.API_URL
import projekt.cloud.piece.pic.api.RequestHeaders.generateHeaders
import projekt.cloud.piece.pic.util.HttpUtil.POST
import projekt.cloud.piece.pic.util.HttpUtil.httpPost

object ApiAuth {

    @Serializable
    data class SignInRequestBody(
        val email: String,
        val password: String
    )

    @Serializable
    data class SignInResponseBody(
        val code: Int,
        val message: String,
        val `data`: Data) {

        @Serializable
        data class Data(
            val token: String
        )

        val token: String
            get() = `data`.token
    }

    fun signIn(account: String, password: String) =
        httpPost(API_URL + API_SIGN_IN,
            generateHeaders(API_SIGN_IN, POST),
            Json.encodeToString(SignInRequestBody(account, password))
        )

}