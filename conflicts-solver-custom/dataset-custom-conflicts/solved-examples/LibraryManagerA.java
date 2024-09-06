public class LibraryManager {
    private int totalBooks = 100;

    public void addBooks(int books) {
        totalBooks += books;
    }

    public int getTotalBooks() {
        return totalBooks;
    }
}


