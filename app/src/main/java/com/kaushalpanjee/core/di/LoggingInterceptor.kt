import android.util.Log
import com.kaushalpanjee.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class LoggingInterceptor : Interceptor {

    private val tag = "API_DEBUG"

    // Debug build me hi token / headers dikhao, release me hamesha mask
    private val showSensitive = BuildConfig.DEBUG

    private val sensitiveHeaders = setOf(
        "authorization", "client_secret", "cookie", "set-cookie", "x-api-key"
    )

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val startNs = System.nanoTime()

        /* ───────── REQUEST ───────── */
        Log.d(tag, "========== API REQUEST ==========")
        Log.d(tag, "Method: ${request.method}")
        Log.d(tag, "URL: ${request.url}")

        request.headers.names().forEach { name ->
            val value = if (isSensitive(name) && !showSensitive) {
                "REDACTED"
            } else {
                request.header(name).orEmpty()
            }
            Log.d(tag, "$name: $value")
        }

        Log.d(tag, "Request Content-Type: ${request.body?.contentType()}")

        val body = request.body
        if (
            body != null &&
            body.contentLength() != 0L &&
            body.contentType()?.type != "multipart"
        ) {
            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                Log.d(tag, "Request Body: ${buffer.readUtf8()}")
            } catch (e: Exception) {
                Log.e(tag, "Unable to read request body")
            }
        }

        /* ───────── EXECUTE ───────── */
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e(tag, "========== API FAILED ==========")
            Log.e(tag, "URL: ${request.url}")
            Log.e(tag, "Exception: ${e.javaClass.simpleName}: ${e.message}")
            throw e
        }

        val tookMs = (System.nanoTime() - startNs) / 1_000_000

        /* ───────── RESPONSE ───────── */
        Log.d(tag, "========== API RESPONSE ==========")
        Log.d(tag, "HTTP Code: ${response.code}")
        Log.d(tag, "Response Message: ${response.message}")
        Log.d(tag, "URL: ${response.request.url}")
        Log.d(tag, "Time: ${tookMs}ms")

        // Response headers (server naya token header me bheje to yaha dikhega)
        response.headers.names().forEach { name ->
            val value = if (isSensitive(name) && !showSensitive) {
                "REDACTED"
            } else {
                response.header(name).orEmpty()
            }
            Log.d(tag, "Response Header -> $name: $value")
        }

        // Body: success aur error dono, original response consume nahi hota
        try {
            val bodyText = response.peekBody(1024 * 1024).string()
            if (response.isSuccessful) {
                logLong(Log.DEBUG, "Response Body", bodyText)
            } else {
                logLong(Log.ERROR, "Error Body", bodyText)
            }
        } catch (e: Exception) {
            Log.e(tag, "Unable to read response body")
        }

        Log.d(tag, "========== END ${response.request.url.encodedPath} ==========")

        return response
    }

    private fun isSensitive(name: String) = name.lowercase() in sensitiveHeaders

    /**
     * Logcat ek line me ~4000 chars hi dikhata hai. Banner response me base64 images
     * hoti hain (bahut bade), isliye body ko limit karte hain aur chunks me print karte hain.
     */
    private fun logLong(priority: Int, label: String, text: String, maxChars: Int = 3000) {
        val shown = if (text.length > maxChars) {
            text.take(maxChars) + "... [truncated, total ${text.length} chars]"
        } else text

        shown.chunked(3500).forEachIndexed { i, part ->
            Log.println(priority, tag, if (i == 0) "$label: $part" else part)
        }
    }
}