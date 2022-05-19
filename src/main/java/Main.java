import java.io.*;

public class Main {
    public static void main(String[] args) {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        var port = 8989;
        Server server = new Server(port, engine);
        server.start();
    }
}