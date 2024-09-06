public class LibraryManager {
    private int totalBooks = 200;

    public void addBooks(int books) {
        totalBooks += books;
    }

    public int getTotalBooks() {
        return totalBooks;
    }
}

