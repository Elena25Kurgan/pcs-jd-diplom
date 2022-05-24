import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
    private final int port;
    BooleanSearchEngine searchEngine;

    public Server(int port, BooleanSearchEngine searchEngine) {
        this.port = port;
        this.searchEngine = searchEngine;
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(this.port)) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                System.out.println("Ожидаем подключения...");
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
                    System.out.println("Пошло подключение");
                    var word = in.readLine();
                    String listJson = new SearchWord(searchEngine).search(word);
                    out.write(listJson);
                    out.close();
                    PrintOut(listJson);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }

    private void PrintOut(String listJson) {
        var printWriter = new PrintStream(System.out);
        printWriter.println(listJson);
    }
}

