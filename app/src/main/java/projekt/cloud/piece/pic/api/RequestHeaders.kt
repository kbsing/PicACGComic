package projekt.cloud.piece.pic.api

import com.fasterxml.uuid.Generators
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object RequestHeaders {

    private const val API_KEY = "C69BAF41DA5ABD1FFEDC6D2FEA56B"
    private const val PRIVATE_KEY = "~d}\$Q7\$eIni=V)9\\RK/P.RM4;9[7|@/CA}b~OW!3?EV`:<>M7pddUBL5n|0/*Cn"

    private const val HEADER_TIME = "time"
    private const val HEADER_NONCE = "nonce"
    private const val HEADER_SIGNATURE = "signature"
    private const val HEADER_AUTHORIZATION = "authorization"

    private val headers = mapOf(
        "api-key" to API_KEY,
        "accept" to "application/vnd.picacomic.com.v1+json",
        "User-Agent" to "okhttp/3.8.1",
        "app-platform" to "android",
        "app-uuid" to "defaultUuid",
        "app-build-version" to "2.2.1.3.3.4",
        "app-channel" to "3",
        "image-quality" to "original",
        "app-build-version" to "45"
    )

    private fun generateRawData(requestApi: String, time: String, nonce: String, method: String) =
        (requestApi + time + nonce + method + API_KEY).lowercase()

    private const val ALGORITHM_SHA256 = "HmacSHA256"
    private val Byte.hex get() = String.format("%02x", this)
    private fun generateSignature(key: ByteArray, data: ByteArray) =
        Mac.getInstance(ALGORITHM_SHA256)
            .apply { init(SecretKeySpec(key, ALGORITHM_SHA256)) }
            .doFinal(data)
            .joinToString("") { it.hex }

    fun generateHeaders(requestApi: String,
                        method: String,
                        authorizedToken: String? = null) =
        headers.toMutableMap().also { headers ->
            val nonceGenerator = Generators.timeBasedGenerator()
                .generate()
            val time = (nonceGenerator.timestamp() / 1000).toString()
            val nonce = nonceGenerator.toString()
                .replace("-", "")
            headers[HEADER_TIME] = time
            headers[HEADER_NONCE] = nonce
            headers[HEADER_SIGNATURE] = generateSignature(
                PRIVATE_KEY.toByteArray(),
                generateRawData(requestApi, time, nonce, method).toByteArray()
            )
            authorizedToken?.let { headers[HEADER_AUTHORIZATION] = it }
        }

}