import java.util.Objects;

public class PageEntry implements Comparable<PageEntry> {
    private String pdfName;
    private int page;
    private int count;

    public PageEntry() {}

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "\n  {" + '\n' +
                "       pdfName='" + pdfName + '\'' + ',' + '\n' +
                "       page=" + page +  ',' + '\n' +
                "       count=" + count + '\n' +
                "   }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageEntry)) return false;
        PageEntry pageEntry = (PageEntry) o;
        return page == pageEntry.page && Objects.equals(pdfName, pageEntry.pdfName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdfName, page, getCount());
    }

    @Override
    public int compareTo(PageEntry o) {
        return o.getCount() - this.getCount();
    }
}
