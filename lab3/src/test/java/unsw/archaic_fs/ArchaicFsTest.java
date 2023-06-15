package unsw.archaic_fs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import unsw.archaic_fs.exceptions.UNSWFileAlreadyExistsException;
import unsw.archaic_fs.exceptions.UNSWFileNotFoundException;
import unsw.archaic_fs.exceptions.UNSWNoSuchFileException;
import java.util.EnumSet;

public class ArchaicFsTest {
    @Test
    public void testCdInvalidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        // Try to change directory to an invalid one
        assertThrows(UNSWNoSuchFileException.class, () -> {
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdValidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdAroundPaths() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
            assertEquals("/usr/bin/cool-stuff", fs.cwd());
            fs.cd("..");
            assertEquals("/usr/bin", fs.cwd());
            fs.cd("../bin/..");
            assertEquals("/usr", fs.cwd());
        });
    }

    @Test
    public void testCreateFile() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            assertEquals("My Content", fs.readFromFile("test.txt"));
            fs.writeToFile("test.txt", "New Content", EnumSet.of(FileWriteOptions.TRUNCATE));
            assertEquals("New Content", fs.readFromFile("test.txt"));
        });
    }

    // Now write some more!
    // Some ideas to spark inspiration;
    // - File Writing/Reading with various options (appending for example)
    // - Cd'ing .. on the root most directory (shouldn't error should just remain on
    // root directory)
    // - many others...

    // test to create a folder with the same name as a already existing folder
    @Test
    public void testCreateValidFolder() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.mkdir("/usr", false, false);
            fs.mkdir("/usr", false, true);
            fs.mkdir("/usr/test", true, false);
        });

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/test", true, true);
            fs.mkdir("/usr/bin", true, true);
        });
    }

    // test to create a folder with a not existing parent folder
    @Test
    public void testCreateInvalidFolder() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.mkdir("/usr/big/test", false, false);
            fs.mkdir("/usr/big/test", false, true);
        });

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/big/test", true, true);
        });
    }

    @Test
    public void testWriteToFile() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.writeToFile("/urs/test.txt", "My Content", EnumSet.of(FileWriteOptions.TRUNCATE));
        });

        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.APPEND));
        });

        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.APPEND));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
        });

        assertDoesNotThrow(() -> {
            fs.writeToFile("/usr/test.txt", "My Content",
                    EnumSet.of(FileWriteOptions.CREATE_IF_NOT_EXISTS, FileWriteOptions.TRUNCATE));
            fs.writeToFile("/usr/test.txt", "My Content",
                    EnumSet.of(FileWriteOptions.CREATE_IF_NOT_EXISTS, FileWriteOptions.APPEND));
        });
    }

    @Test
    public void testReadFromFile() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.readFromFile("/urs/test.txt");
        });

        assertDoesNotThrow(() -> {
            fs.writeToFile("/usr/test.txt", "My Content",
                    EnumSet.of(FileWriteOptions.CREATE_IF_NOT_EXISTS, FileWriteOptions.TRUNCATE));
            assertEquals("My Content", fs.readFromFile("/usr/test.txt"));
        });
    }
}
