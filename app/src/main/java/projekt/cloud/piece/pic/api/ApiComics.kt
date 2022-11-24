package projekt.cloud.piece.pic.api

import kotlinx.serialization.Serializable
import projekt.cloud.piece.pic.api.CommonBody.Thumb
import projekt.cloud.piece.pic.api.PicApi.API_COMICS
import projekt.cloud.piece.pic.api.PicApi.API_URL
import projekt.cloud.piece.pic.api.RequestHeaders.generateHeaders
import projekt.cloud.piece.pic.util.HttpUtil.GET
import projekt.cloud.piece.pic.util.HttpUtil.asParams
import projekt.cloud.piece.pic.util.HttpUtil.httpGet

object ApiComics {

    private const val PARAM_PAGE = "page"
    private const val PARAM_CATEGORY = "c"
    private const val PARAM_SORT = "s"

    @Serializable
    data class ComicsResponseBody(val code: Int,
                                  val message: String,
                                  val data: Data) {

        @Serializable
        data class Data(val comics: Comics) {

            @Serializable
            data class Comics(val docs: List<Docs>,
                              val total: Int,
                              val limit: Int,
                              val page: Int,
                              val pages: Int) {

                @Serializable
                data class Docs(
                    val _id: String,
                    val title: String,
                    val author: String,
                    val totalViews: Int,
                    val totalLikes: Int,
                    val pagesCount: Int,
                    val epsCount: Int,
                    val finished: Boolean,
                    val categories: List<String>,
                    val thumb: Thumb,
                    val id: String,
                    val likesCount: Int
                )

            }

        }

    }

    private fun comicsParams(page: Int, category: String, sort: String) =
        mapOf(PARAM_PAGE to page.toString(), PARAM_CATEGORY to category, PARAM_SORT to sort).asParams

    fun comics(token: String, page: Int, category: String, sort: String) =
        (API_COMICS + comicsParams(page, category, sort)).let {
            httpGet(API_URL + it, generateHeaders(it, GET, token))
        }

}