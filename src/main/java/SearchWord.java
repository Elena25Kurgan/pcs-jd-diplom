import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SearchWord {
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

    public String search(String word) {
        String listJson = "";
        try {
            var listPageEntry = searchEngine.search(word);
            listJson = listToJson(listPageEntry);
        } catch (Exception e) {
            System.out.println("Произошла ошибка поиска!");
            e.printStackTrace();
        }
        return listJson;
    }
}
