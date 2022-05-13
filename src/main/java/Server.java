import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 8989;
    private static final int BUFFER_SIZE = 200;

    private int port;
    private String directory;
    BooleanSearchEngine searchEngine;
    private HttpServer httpServer;


    public Server(int port, String directory, BooleanSearchEngine searchEngine) throws Exception {
        this.port = port;
        this.directory = directory;
        this.searchEngine = searchEngine;
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:8989/");
        try (var server = new ServerSocket(this.port)) {
            while (true) {
                var socket = server.accept();
//                var thread = new Handler(socket, this.directory);
                var thread = new Handler(server, socket);
                thread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        var word = new BufferedReader(new InputStreamReader(h.getRequestBody())).readLine();
        try {
            var listPageEntry = searchEngine.search(word);
            String listJson = listToJson(listPageEntry);
            h.sendResponseHeaders(200, 0);
            try (BufferedOutputStream out = new BufferedOutputStream(h.getResponseBody())) {
                try (ByteArrayInputStream bis = new ByteArrayInputStream(listJson.getBytes())) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count;
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
