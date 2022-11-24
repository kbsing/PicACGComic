package projekt.cloud.piece.pic.api

import kotlinx.serialization.Serializable

object CommonBody {

    @Serializable
    data class Thumb(
        val originalName: String,
        val path: String,
        val fileServer: String
    )

}