package projekt.cloud.piece.pic.api

import kotlinx.serialization.Serializable
import projekt.cloud.piece.pic.api.CommonBody.Image
import projekt.cloud.piece.pic.api.PicApi.API_URL
import projekt.cloud.piece.pic.api.PicApi.API_USER_PROFILE
import projekt.cloud.piece.pic.api.RequestHeaders.generateHeaders
import projekt.cloud.piece.pic.util.HttpUtil.GET
import projekt.cloud.piece.pic.util.HttpUtil.httpGet

object ApiUser {

    @Serializable
    data class ProfileResponseBody(
        val code: Int,
        val message: String,
        val `data`: Data) {

        @Serializable
        data class Data(val user: User) {

            @Serializable
            data class User(
                val _id: String,
                val birthday: String,
                val characters: List<String>,
                val created_at: String,
                val email: String,
                val exp: Int,
                val gender: String,
                val isPunched: Boolean,
                val level: Int,
                val name: String,
                val slogan: String = "",
                val title: String,
                val verified: Boolean,
                val avatar: Image? = null
            )

        }

    }

    fun userProfile(token: String) =
        httpGet(API_URL + API_USER_PROFILE, generateHeaders(API_USER_PROFILE, GET, token))

}