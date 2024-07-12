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

    public void displayLibraryInfo() {
        System.out.println("Library Details - Name: " + libraryName + ", Location: " + location);
    }

    public String fetchLibraryDetails() {
        return "Library [Name: " + libraryName + ", Location: " + location + "]";
    }
}

