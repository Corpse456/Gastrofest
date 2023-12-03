package by.gastrofest.utils;

import by.gastrofest.exception.ConnectionException;
import org.codehaus.httpcache4j.uri.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final String POST = "POST";

    private static final String PUT = "PUT";

    private static final String GET = "GET";

    private static final String DELETE = "DELETE";

    public static Document getDocument(final String url) {
        final String content = HttpUtil.sendGet(url);
        return Jsoup.parse(content);
    }

    public static String getEncodedString(final String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            final var stream = new BufferedInputStream(url.openConnection().getInputStream());

            return Base64.getEncoder().encodeToString(stream.readAllBytes());
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
    }

    public static String getUrlWithParameters(Map<String, List<String>> properties, final String url) {
        return URIBuilder.fromString(url).addParameters(properties).toAbsoluteURI().toString();
    }

    public static String sendPost(final String urlPath,
            final String value,
            final HashMap<String, String> property) {
        return connect(POST, urlPath, value, property);
    }

    public static String sendPut(final String urlPath,
            final String value,
            final HashMap<String, String> property) {
        return connect(PUT, urlPath, value, property);
    }

    public static String sendGet(final String urlPath,
            final HashMap<String, String> property) {
        return connect(GET, urlPath, null, property);
    }

    public static String sendGet(final String urlPath) {
        return connect(GET, urlPath, null, new HashMap<>());
    }

    private static String connect(final String post,
            final String urlPath,
            final String value,
            final HashMap<String, String> property) {
        try {
            final var url = new URL(urlPath);
            final var connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod(post);
            property.forEach(connection::setRequestProperty);

            if (value != null) {
                connection.setDoOutput(true);
                final var outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(value);
                outputStream.flush();
                outputStream.close();
            }

            final var status = connection.getResponseCode();
            final var body = getBody(status, connection);
            if (successfulResponse(status)) {
                return body;
            }
            throw new ConnectionException(body, status);
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    private static String getBody(final int status, final HttpsURLConnection connection) throws IOException {
        final var reader = successfulResponse(status)
                ? new BufferedReader(new InputStreamReader(connection.getInputStream()))
                : new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String inputLine;
        final var response = new StringBuilder();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response.toString();
    }

    private static boolean successfulResponse(final int status) {
        return String.valueOf(status).charAt(0) == '2';
    }

}
