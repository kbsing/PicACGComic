package projekt.cloud.piece.pic.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object SerializableUtil {
    
    @JvmStatic
    inline fun <reified T> String.decodeJson() =
        Json.decodeFromString<T>(this)
    
    @JvmStatic
    inline val <reified T> T.encodeJson: String
        get() = Json.encodeToString<T>(this)
    
}