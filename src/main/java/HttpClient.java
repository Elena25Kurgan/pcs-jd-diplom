import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpClient {
    private final Socket clientSocket;

    public HttpClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String sendRequest() {
        String word = "";
        try {
            final InputStreamReader reader = new InputStreamReader(this.clientSocket.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(reader);
            if ((bufferedReader.readLine()) != null) {
                word = bufferedReader.readLine();
                System.out.println("word = " + word);
            }
            else {
                System.out.println("Ошибка программы");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return word;
    }
}
