package by.gastrofest.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val GET = "GET"

fun getDocument(url: String): Document {
    val content: String = sendGet(url)
    return Jsoup.parse(content)
}

private fun sendGet(urlPath: String): String {
    val url = URL(urlPath)
    val connection = url.openConnection() as HttpsURLConnection

    connection.requestMethod = GET

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
