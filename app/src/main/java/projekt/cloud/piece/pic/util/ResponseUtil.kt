package projekt.cloud.piece.pic.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Response

object ResponseUtil {
    
    @JvmStatic
    inline fun <reified T> Response.decodeJson() =
        Json.decodeFromString<T>(this.body.string())

}