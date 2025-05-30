package com.gadeadiaz.physiocare.utils;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.responses.ErrorResponse;
import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.util.Pair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.zip.GZIPInputStream;

/**
 * Utility class for handling HTTP service communications, including
 * synchronous and asynchronous requests, token-based authentication,
 * and charset detection.
 */
public class ServiceUtils {
    private static final Dotenv dotenv = Dotenv.load();
    public static final String SERVER = dotenv.get("API_URL");

    /**
     * Extracts the charset (e.g., UTF-8, ISO-8859-1) from a Content-Type header string.
     *
     * @param contentType the Content-Type header value
     * @return the charset string if found; otherwise, null
     */
    public static String getCharset(String contentType) {
        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                return param.split("=", 2)[1];
            }
        }
        return null; // Probably binary content
    }

    /**
     * Performs a synchronous HTTP request to the specified URL using the given method and optional payload.
     * Automatically includes headers and authentication token if set.
     *
     * @param url the target URL
     * @param data the request body (optional, used for POST/PUT methods)
     * @param method the HTTP method to use (e.g., "GET", "POST")
     * @return the server's response as a string
     */
    public static String getResponse(String url, String data, String method) {
        BufferedReader bufInput = null;
        StringJoiner result = new StringJoiner("\n");
        try {
            URL urlConn = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlConn.openConnection();
            conn.setReadTimeout(20000); // milliseconds
            conn.setConnectTimeout(15000); // milliseconds
            conn.setRequestMethod(method);

            // Set headers
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            conn.setRequestProperty("Accept-Language", "es-ES,es;q=0.8");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");

            Pair<String, String> userdata = Storage.getInstance().getUserdata();
            if (userdata != null) {
                conn.setRequestProperty("Authorization", "Bearer " + userdata.getKey());
            }

            // Send request data if provided
            if (data != null) {
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.write(data.getBytes());
                    wr.flush();
                }
            }

            if (conn.getResponseCode() >= 400) {
                try (BufferedReader errorReaderBuffer = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream())
                )) {
                    String line;
                    while ((line = errorReaderBuffer.readLine()) != null) {
                        result.add(line);
                    }
                    ErrorResponse errorResponse = new Gson().fromJson(result.toString(), ErrorResponse.class);
                    throw new RequestErrorException(errorResponse);
                }
            } else {
                if (conn.getResponseCode() != 204) {
                    // Handle successful response
                    String charset = getCharset(conn.getHeaderField("Content-Type"));
                    if (charset != null) {
                        InputStream input = conn.getInputStream();
                        if ("gzip".equals(conn.getContentEncoding())) {
                            input = new GZIPInputStream(input);
                        }

                        bufInput = new BufferedReader(new InputStreamReader(input));
                        String line;
                        while ((line = bufInput.readLine()) != null) {
                            result.add(line);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (bufInput != null) {
                try {
                    bufInput.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return result.toString();
    }

    /**
     * Executes an HTTP request asynchronously using a CompletableFuture.
     * Wraps the synchronous {@link #getResponse(String, String, String)} method.
     *
     * @param url the target URL
     * @param data the request body (optional)
     * @param method the HTTP method to use
     * @return a CompletableFuture containing the server response
     */
    public static CompletableFuture<String> getResponseAsync(String url, String data, String method) {
        return CompletableFuture.supplyAsync(() -> getResponse(url, data, method));
    }
}