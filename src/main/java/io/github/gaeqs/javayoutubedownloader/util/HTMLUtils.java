package io.github.gaeqs.javayoutubedownloader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtils {

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36";

    /**
     * Reads all the text from an {@link URL}.
     *
     * @param url the url.
     * @return the text.
     * @throws IOException whether an IO exception is thrown.
     */
    public static String readAll(URL url) throws IOException {

        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setDoInput(true);

        InputStream is = connection.getInputStream();
        String enc = connection.getContentEncoding();
        if (enc == null) {
            Pattern p = Pattern.compile("charset=(.*)");
            Matcher m = p.matcher(connection.getHeaderField("Content-Type"));
            if (m.find()) enc = m.group(1);
            else enc = "UTF-8";
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, enc));

        String line;
        StringBuilder html = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            html.append(line).append("\n");
            if (Thread.currentThread().isInterrupted())
                throw new RuntimeException("HTML download has been interrupted.");
        }

        return html.toString();
    }

    /**
     * Checks if a connection is valid.
     *
     * @param c the connection.
     * @throws IOException if the connection is not valid.
     */
    public static void check(HttpURLConnection c) throws IOException {
        int code = c.getResponseCode();
        String message = c.getResponseMessage();

        switch (code) {
            case HttpURLConnection.HTTP_OK:
            case HttpURLConnection.HTTP_PARTIAL:
                return;
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_MOVED_PERM:
                // rfc2616: the user agent MUST NOT automatically redirect the
                // request unless it can be confirmed by the user
                throw new RuntimeException("Download moved" + " (" + message + ")");
            case HttpURLConnection.HTTP_PROXY_AUTH:
                throw new RuntimeException("Proxy auth" + " (" + message + ")");
            case HttpURLConnection.HTTP_FORBIDDEN:
                throw new RuntimeException("Http forbidden: " + code + " (" + message + ")");
            case 416:
                throw new RuntimeException("Requested range nt satisfiable" + " (" + message + ")");
        }
    }

}
