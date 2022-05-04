
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Сервер уже за вас написан, его трогать не надо :)
 */
public class GServer {
    public static final int PORT = 8889;
    private static final int BUFFER_SIZE = 200;

    private final HttpServer server;
    private final BooleanSearchEngine searchEngine;

    public GServer(BooleanSearchEngine searchEngine) throws Exception {
        if (searchEngine == null) {
            throw new IllegalArgumentException("Серверу нужно передать в конструктор объект-поиска, а было передано null.");
        }
        this.searchEngine = searchEngine;

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/", this::serveHtml);
        server.createContext("/convert", this::serveSearch);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:8889/");
        server.start();
    }

    protected void serveHtml(HttpExchange h) throws IOException {
        System.out.println("Serving html..");
        var htmlPath = Path.of("assets/index.html");
        var htmlContent = Files.readString(htmlPath);
        var jsPath = Path.of("assets/my.js");
        var jsContent = Files.readString(jsPath);
        htmlContent = htmlContent.replace("{{{JS}}}", jsContent);
        var htmlBytes = htmlContent.getBytes();
        h.sendResponseHeaders(200, htmlBytes.length);
        h.getResponseBody().write(htmlBytes);
        h.close();
    }

    private static String listToJson(List<PageEntry> list) {
        Type listType = new TypeToken<List<PageEntry>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(list, listType);
    }

    protected void serveSearch(HttpExchange h) throws IOException {
        System.out.println("Поиск...");
        var word = new BufferedReader(new InputStreamReader(h.getRequestBody())).readLine();
        try {
            System.out.println("Ищем слово: " + word);
            var listPageEntry = searchEngine.search(word);
            String listJson = listToJson(listPageEntry);
            h.sendResponseHeaders(200, 0);
            //решение вопроса "Слишком много байтов в потоке"
            try (BufferedOutputStream out = new BufferedOutputStream(h.getResponseBody())) {
                try (ByteArrayInputStream bis = new ByteArrayInputStream(listJson.getBytes())) {
                    byte [] buffer = new byte [BUFFER_SIZE];
                    int count ;
                    while ((count = bis.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            var msg = e.getMessage();
            if (msg.isEmpty()) {
                msg = "Произошла ошибка поиска :'(";
            }
            var msgBytes = msg.getBytes();
            h.sendResponseHeaders(500, msgBytes.length);
            h.getResponseBody().write(msgBytes);
        } finally {
            h.close();
        }
    }
}
