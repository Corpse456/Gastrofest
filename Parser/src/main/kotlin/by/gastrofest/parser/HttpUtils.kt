package by.gastrofest.parser

import org.codehaus.httpcache4j.uri.URIBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

private const val POST = "POST"
private const val PUT = "PUT"
private const val GET = "GET"
private const val DELETE = "DELETE"

fun getDocument(url: String): Document {
    val content: String = sendGet(url)
    return Jsoup.parse(content)
}

fun getEncodedString(imageUrl: String?): String {
    val url = URL(imageUrl)
    val stream = BufferedInputStream(url.openConnection().getInputStream())

    return Base64.getEncoder().encodeToString(stream.readAllBytes())
}

fun getUrlWithParameters(properties: Map<String?, List<String?>?>?, url: String?): String {
    return URIBuilder.fromString(url).addParameters(properties).toAbsoluteURI().toString()
}

fun sendPost(urlPath: String, value: String?, property: HashMap<String, String>): String {
    return connect(POST, urlPath, value, property)
}

fun sendPut(urlPath: String, value: String?, property: HashMap<String, String>): String {
    return connect(PUT, urlPath, value, property)
}

fun sendGet(urlPath: String, property: HashMap<String, String>): String {
    return connect(GET, urlPath, null, property)
}

fun sendGet(urlPath: String): String {
    return connect(GET, urlPath, null, HashMap())
}

private fun connect(
    post: String,
    urlPath: String,
    value: String?,
    property: HashMap<String, String>
): String {
    val url = URL(urlPath)
    val connection = url.openConnection() as HttpsURLConnection

    connection.requestMethod = post
    property.forEach { (key: String?, value: String?) -> connection.setRequestProperty(key, value) }

    if (value != null) {
        connection.doOutput = true
        val outputStream = DataOutputStream(connection.outputStream)
        outputStream.writeBytes(value)
        outputStream.flush()
        outputStream.close()
    }

    val status = connection.responseCode
    val body = getBody(status, connection)
    if (successfulResponse(status)) {
        return body
    }
    error("Body: $body, status: $status")
}

private fun getBody(status: Int, connection: HttpsURLConnection): String {
    val reader = if (successfulResponse(status)) {
        BufferedReader(InputStreamReader(connection.inputStream))
    } else {
        BufferedReader(InputStreamReader(connection.errorStream))
    }
    var inputLine: String?
    val response = StringBuilder()
    while ((reader.readLine().also { inputLine = it }) != null) {
        response.append(inputLine)
    }
    reader.close()
    return response.toString()
}

private fun successfulResponse(status: Int): Boolean {
    return status.toString()[0] == '2'
}
