package unsw.archaic_fs.exceptions;

public class UNSWNoSuchFileException extends java.io.FileNotFoundException {
    public UNSWNoSuchFileException(String message) {
        super(message);
    }
}
