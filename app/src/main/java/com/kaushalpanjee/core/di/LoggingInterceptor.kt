import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer

class LoggingInterceptor : Interceptor {

    private val TAG = "API_LOG"

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response: Response

        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d(TAG, "=====================REQUEST========================================================")
        Log.d(TAG, "URL: ${request.url}\n")
        Log.d(TAG, "Method: ${request.method}\n")
        Log.d(TAG, "Headers: ${request.headers}\n")

        val gson = GsonBuilder().setPrettyPrinting().create()
        request.body?.let {
            val jsonResponse = gson.toJson(it)
            Log.d(TAG, "Body: ${jsonResponse}\n")
            Log.d(TAG, "Body: ${bodyToString(it)}\n")
        }

        response = chain.proceed(request)

       // val copy = response.peekBody(Long.MAX_VALUE)
        val copy = response.peekBody(1024 * 1024)
        Log.d(TAG, "======================= RESPONSE========================================================")
        Log.d(TAG, "Code: ${response.code}\n")
        Log.d(TAG, "URL: ${response.request.url}\n")
        Log.d(TAG, "Body: ${copy.string()}\n")

        //  Log.d(TAG, "Message : ${ response.message}\n")


        Log.d(TAG, "━━━━━━━━━━━━━━━━━END━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")

        return response
    }

    private fun bodyToString(body: RequestBody): String {
        return try {
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "Unable to read body"
        }
    }
}
