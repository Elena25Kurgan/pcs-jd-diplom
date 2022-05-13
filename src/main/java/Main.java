import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
//        int port = 8989;
//        ServerSocket serverSocket = new ServerSocket(port);
//        System.err.println("Сервер, запущенный через порт : " + port);
//        while (true) {
//            Socket clientSocket = serverSocket.accept();
//            System.err.println("Новый подключенный клиент");
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//            String s;
//            while ((s = in.readLine()) != null) {
//                System.out.println(s);
//                if (s.isEmpty()) {
//                    break;
//                }
//            }
//            // отправляем ответ
//            out.write("HTTP/1.1 200 OK");
//            out.write("Content-Type: text/html; charset=utf-8");
//            out.write("<p>Привет всем!</p>");
//            out.flush();
//            System.err.println("Соединение с клиентом завершено");
//            out.close();
//            in.close();
//            clientSocket.close();
//        }
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        var port = 8989;
        var directory = "assets/index.html";
        new Server(port,directory,engine).start();


        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
//        GServer server = new GServer(engine); // Создаём объект сервера
//        server.start(); // Запускаем

//        URL url = new URL("https://www.youtube.com");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//        connection.setRequestMethod("POST");
//        Map params = new HashMap<>();
//        params.put("v", "dQw4w9WgXcQ");
//
//        StringBuilder postData = new StringBuilder();
//        for (Map.Entry param : params.entrySet()) {
//            if (postData.length() != 0) {
//                postData.append('&');
//            }
//            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//            postData.append('=');
//            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//        }
//
//        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//        connection.setDoOutput(true);
//        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
//            writer.write(postDataBytes);
//            writer.flush();
//            writer.close();
//
//            StringBuilder content;
//
//            try (BufferedReader in = new BufferedReader(
//                    new InputStreamReader(connection.getInputStream()))) {
//                String line;
//                content = new StringBuilder();
//                while ((line = in.readLine()) != null) {
//                    content.append(line);
//                    content.append(System.lineSeparator());
//                }
//            }
//            System.out.println(content.toString());
//        } finally {
//            connection.disconnect();
//        }
//    }
    }
}