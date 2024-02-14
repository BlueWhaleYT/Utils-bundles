enum class HttpMethod {
    Get
}

private typealias UrlString = String
private var client = OkHttpClient().newBuilder()
    .build()

fun UrlString.startOkhttpRequest(
    method: HttpMethod,
    configuration: Configuration? = null,
    onResponse: ((Call, Response, String?) -> Unit)? = null,
    onFailure: ((Call, Exception) -> Unit)? = null,
) {
    val requestBuilder = Request.Builder().url(this)

    val request = when (method) {
        HttpMethod.Get -> requestBuilder.build()
    }
    start(
        request = request,
        onResponse = onResponse,
        onFailure = onFailure
    )

}

private fun start(
    request: Request,
    onResponse: ((Call, Response, String?) -> Unit)? = null,
    onFailure: ((Call, Exception) -> Unit)? = null,
) {
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure?.invoke(call, e)
        }
        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            onResponse?.invoke(call, response, responseBody)
        }
    })
}
