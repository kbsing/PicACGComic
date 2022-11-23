package projekt.cloud.piece.pic.api

import kotlinx.serialization.Serializable
import projekt.cloud.piece.pic.api.PicApi.API_CATEGORIES
import projekt.cloud.piece.pic.api.PicApi.API_URL
import projekt.cloud.piece.pic.api.RequestHeaders.generateHeaders
import projekt.cloud.piece.pic.util.HttpUtil.GET
import projekt.cloud.piece.pic.util.HttpUtil.httpGet

object ApiCategories {

    private const val THUMB_URL_DIVIDER = "/static/"

    @Serializable
    data class CategoriesResponseBody(val code: Int, val message: String, val data: Data) {

        @Serializable
        data class Data(val categories: List<Category>) {

            @Serializable
            data class Category(val _id: String? = null,
                                val title: String,
                                val description: String? = null,
                                val thumb: Thumb,
                                val isWeb: Boolean = false,
                                val active: Boolean = true,
                                val link: String? = null) {

                @Serializable
                data class Thumb(
                    val originalName: String,
                    val path: String,
                    val fileServer: String
                )

            }
        }
    }

    fun categories(token: String) =
        httpGet(API_URL + API_CATEGORIES, generateHeaders(API_CATEGORIES, GET, token))

    fun thumb(category: CategoriesResponseBody.Data.Category) =
        httpGet(category.thumb.fileServer.run {
            when {
                this.contains(THUMB_URL_DIVIDER) -> this
                else -> "$this$THUMB_URL_DIVIDER"
            }
        } + category.thumb.path)

}