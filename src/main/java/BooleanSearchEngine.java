import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

//Реализация поискового движка, которую вам предстоит написать. Слово
//Boolean пришло из теории информационного поиска, тк наш движок будет
//искать в тексте ровно то слово, которое было указано, без использования
//синонимов и прочих приёмов нечётного поиска
public class BooleanSearchEngine implements SearchEngine {
    HashMap<String, List<PageEntry>> pageEntryHashMap = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        if (pdfsDir.isDirectory()) {
            // получаем все файлы в каталоге
            File[] files = pdfsDir.listFiles();
            if (files != null) {
                for (File item : files) {
                    try (PdfReader pdfReader = new PdfReader(item)) {
                        PdfDocument doc = new PdfDocument(pdfReader);
                        int length = doc.getNumberOfPages();
                        for (int i = 1; i < length; i++) {
                            var page = doc.getPage(i);
                            var text = PdfTextExtractor.getTextFromPage(page);
                            var words = text.split("\\P{IsAlphabetic}+");
                            Map<String, Integer> countWords = countingNumberWords(words);
                            for (String word : words) {
                                if (word.equals("")) {
                                    continue;
                                }
                                PageEntry pageEntry = new PageEntry(item.getName(), i, countWords.get(word.toLowerCase()));
                                List<PageEntry> listPage;
                                if (pageEntryHashMap.get(word) == null) {
                                    listPage = new ArrayList<>();
                                    listPage.add(pageEntry);
                                } else {
                                    listPage = pageEntryHashMap.get(word);
                                    if (!(listPage.contains(pageEntry))) {
                                        listPage.add(pageEntry);
                                    }
                                }
                                Collections.sort(listPage);
                                pageEntryHashMap.put(word, listPage);
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("Ошибка: " + ex.getMessage());
                    }

                }
            }
        }
    }

    //подсчёта частоты слов
    private Map<String, Integer> countingNumberWords(String[] words) {
        Map<String, Integer> freqs = new HashMap<>();
        for (var word : words) {
            if (word.isEmpty()) {
                continue;
            }
            int num = freqs.getOrDefault(word.toLowerCase(), 0) + 1;
            freqs.put(word.toLowerCase(), num);
        }
        return freqs;
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        if ((pageEntryHashMap.get(word)) != null) {
            return pageEntryHashMap.get(word);
        } else {
            System.out.println("Слова " + word + " не найдено");
            return null;
        }
    }
}
