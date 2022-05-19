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
        try (ServerSocket server = new ServerSocket(this.port)) {
            System.out.println("Запускаем сервер на порту " + port);
            while (true) {
                Client client = new Client(port);
                client.run();
                try (Socket socket = server.accept();
                     var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    var word = "";
                    while (in.ready()) {
                        word = in.readLine();
                        if (word.equals("end")) {
                            break;
                        }
                        String listJson = new SearchWord(searchEngine).search(word);
                        out.write(listJson);
                        out.flush();
                        out.close();
                        PrintOut(listJson);
                    }
                    if (word.equals("end")) {
                        System.out.println("Поиск закончен, программа завершена!");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

