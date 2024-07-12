public class Library2 {
    private String libraryName;
    private String location;

    public Library2(String libraryName, String location) {
        this.libraryName = libraryName;
        this.location = location;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLocation() {
        return location;
    }

    public void printLibraryInfo() {
        System.out.println("Library: " + libraryName + ", Location: " + location);
    }

    public String getLibraryDetails() {
        return "Library Name: " + libraryName + ", Location: " + location;
    }
}

