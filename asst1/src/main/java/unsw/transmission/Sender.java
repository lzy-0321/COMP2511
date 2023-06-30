package unsw.transmission;

import java.util.Map;
import java.util.List;

import unsw.file.*;

public interface Sender {
    File getFile(String fileName);

    void addFileToSendSchedule(File file, String id);

    List<File> getFileList();

    boolean isFileInList(File file, List<File> list);

    boolean isFileComplete(File file);

    boolean isFileSending(File file);

    boolean hasBandwidthSend();

    boolean hasBandwidthReceive();

    int getSendSpeed();

    Map<File, String> getSendSchedule();

    File setSendFile(File file, int index, int size);

    File setSendFile(File file, int index);

    File setSendFileWithoutT(File file, int index);

    void sendingFile(File file, boolean isComplete);

    void stopSendingFile(File file);

    void removeTLetter(File file);

    // Map<File, String> getTempFile();

}
