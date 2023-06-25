package unsw.transmission;

import java.util.List;

import unsw.file.*;

public interface Receiver {
    boolean isFileReceiving(File file);

    boolean isFileComplete(File file);

    boolean hasRoom(File file);

    boolean hasBandwidthSend();

    boolean hasBandwidthReceive();

    void addFileInfo(File file);

    int getReceiveSpeed();

    List<File> getFileList();

    boolean isFileInList(File file, List<File> list);

    boolean isFileCompleteInThisTransfering(File file, int index, int size);

    int getCurrentFileSize(File file);

    void receivingFile(File file);

    void stopReceivingFile(File file);
}
