package projekt.cloud.piece.pic.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.serialization.Serializable
import projekt.cloud.piece.pic.util.HttpUtil.httpGet

object CommonBody {

    @Serializable
    data class Thumb(
        val originalName: String,
        val path: String,
        val fileServer: String
    )
    
    private const val THUMB_URL_DIVIDER = "/static/"
    val Thumb.bitmap: Bitmap?
        get() = httpGet(fileServer + THUMB_URL_DIVIDER + path)?.body?.byteStream()?.let {
            BitmapFactory.decodeStream(it)
        }

}