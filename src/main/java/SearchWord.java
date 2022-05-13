import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

public class SearchWord {
    private static final int BUFFER_SIZE = 200;

    private final BooleanSearchEngine searchEngine;

    public SearchWord(BooleanSearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    private static String listToJson(List<PageEntry> list) {
        Type listType = new TypeToken<List<PageEntry>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(list, listType);
    }

    public String search(String word, OutputStream out) {
        try {
            var listPageEntry = searchEngine.search(word);
            String listJson = listToJson(listPageEntry);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(listJson.getBytes())) {
                byte [] buffer = new byte [BUFFER_SIZE];
                int count ;
                while ((count = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.close();
                return buffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            var msg = e.getMessage();
            if (msg.isEmpty()) {
                msg = "Произошла ошибка поиска :'(";
                return msg;
            }
            var msgBytes = msg.getBytes();
        //    h.sendResponseHeaders(500, msgBytes.length);
        //    h.getResponseBody().write(msgBytes);
        } finally {
            return "";
          //  h.close();
        }
    }
}
