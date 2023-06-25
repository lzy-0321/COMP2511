package unsw.file;

public class File {
    private String filename;
    private String content;
    private int size;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.size = content.length();
    }

    public String getFilename() {
        return this.filename;
    }

    public String getContent() {
        return this.content;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addContent(String content) {
        this.content += content;
    }

    public int getCurrentSize() {
        return this.content.length();
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
