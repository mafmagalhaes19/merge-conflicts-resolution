public class Library1 {
    private String libraryName;
    private String location;

    public Library1(String libraryName, String location) {
        this.libraryName = libraryName;
        this.location = location;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLocation() {
        return location;
    }

    // This method should print library info
    public void printLibraryInfo() {
        System.out.println("Library: " + libraryName + ", Location: " + location);
    }

    public String getLibraryDetails() {
        return "Library Name: " + libraryName + ", Location: " + location;
    }
}

