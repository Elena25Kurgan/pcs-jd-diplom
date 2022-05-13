import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Handler extends Thread {

    private final Socket socket;
    private final ServerSocket serverSocket;

    public Handler(ServerSocket serverSocket, Socket socket) throws IOException {
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            var inputStream = this.socket.getInputStream();
            var output = this.socket.getOutputStream();
            var buffer = new BufferedReader(new InputStreamReader(inputStream));
            var br = buffer.readLine();
            if (!(br == null)) {
                var url1 = br.split(" ")[1];
                var filePath = Path.of("assets/index.html" + url1);
                if (url1.equals("/search")) {
                    HttpClient httpClient = new HttpClient(socket);
                    String word = httpClient.sendRequest();
                    BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
                    new SearchWord(engine).search(word, output);
                }
                var fileBytes = Files.readAllBytes(filePath);
                var ps = new PrintStream(output);
                ps.printf("HTTP/1.1 %s %s%n", 200, "text/html");
                ps.printf("Content-type: %s%n", "OK");
                ps.printf("Content-length: %s%n%n", fileBytes.length);
                output.write(fileBytes);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
