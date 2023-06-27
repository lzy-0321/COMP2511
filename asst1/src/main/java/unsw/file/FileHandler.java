package unsw.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.FileInfoResponse;

public class FileHandler {
    private List<File> fileList;
    private Map<File, String> sendSchedule;

    public FileHandler() {
        this.fileList = new ArrayList<File>();
        this.sendSchedule = new HashMap<File, String>();
    }

    public boolean isFileListEmpty() {
        return fileList.isEmpty();
    }

    public File getFile(String filename) {
        for (File file : fileList) {
            if (file.getFilename().equals(filename)) {
                return file;
            }
        }
        return null;
    }

    public int getFileSize(File file) {
        return file.getSize();
    }

    public List<File> getFileList() {
        if (fileList == null) {
            this.fileList = new ArrayList<File>();
        }
        return fileList;
    }

    public List<File> getReceivingList() {
        List<File> list = new ArrayList<>();
        for (File file : fileList) {
            if (!isFileComplete(file)) {
                list.add(file);
            }
        }
        return list;
    }

    public void setFile(File file) {
        // filenames can be presumed to be unique
        fileList.add(file);
    }

    public void removeFile(String filename) {
        List<File> files = getFileList();
        for (File file : files) {
            if (file.getFilename().equals(filename)) {
                files.remove(file);
                break;
            }
        }
    }

    public int getCurrentFileSize(File file) {
        File currentFile = getFile(file.getFilename());
        return currentFile.getCurrentSize();
    }

    public int getFileListSize() {
        return fileList.size();
    }

    public int getAllFilesSize() {
        return fileList.size();
    }

    public int getAllContentSize() {
        int size = 0;
        for (File file : fileList) {
            size += file.getSize();
        }
        for (File file : sendSchedule.keySet()) {
            size += file.getSize();
        }
        return size;
    }

    public int getReceivingSize() {
        int size = 0;
        for (File file : fileList) {
            if (!isFileComplete(file)) {
                size++;
            }
        }
        return size;
    }

    public int getFileSizeInList(List<File> list, File file) {
        String fileName = file.getFilename();
        for (File existingFile : list) {
            if (existingFile.getFilename().equals(fileName)) {
                return existingFile.getCurrentSize();
            }
        }
        return 0;
    }

    // get a file inside a list
    public File getFileInList(List<File> list, File file) {
        String fileName = file.getFilename();
        for (File existingFile : list) {
            if (existingFile.getFilename().equals(fileName)) {
                return existingFile;
            }
        }
        return null;
    }

    // return the file info map
    public Map<String, FileInfoResponse> getFileInfoMap() {
        // add file info to the file info map
        Map<String, FileInfoResponse> fileInfoMap = new HashMap<>();
        for (File file : getFileList()) {
            boolean isComplete = isFileComplete(file);
            FileInfoResponse fileInfo = new FileInfoResponse(file.getFilename(), file.getContent(), file.getSize(),
                    isComplete);
            fileInfoMap.put(file.getFilename(), fileInfo);
        }
        return fileInfoMap;
    }

    public boolean isFileInSchedule(File file, String toId) {
        // check if the file is in the schedule with the same toId
        if (sendSchedule.containsKey(file) && sendSchedule.get(file).equals(toId)) {
            return true;
        }
        return false;
    }

    public boolean isFileSending(File file) {
        // check if the file is in the sending list
        if (sendSchedule.containsKey(file)) {
            return true;
        }
        return false;
    }

    public void addFileToSendSchedule(File file, String toId) {
        sendSchedule.put(file, toId);
    }

    public void removeFileFromSendSchedule(File file) {
        sendSchedule.remove(file);
    }

    public Map<File, String> getSendSchedule() {
        return sendSchedule;
    }

    public int getSendScheduleSize() {
        return sendSchedule.size();
    }

    public void addFileInfo(File file) {
        // add file into fileList without content
        File newFile = new File(file.getFilename(), "");
        newFile.setSize(file.getSize());
        fileList.add(newFile);
    }

    public boolean isFileComplete(File file) {
        if (file.getSize() == file.getCurrentSize()) {
            return true;
        }
        return false;
    }

    // return the file content from index to index + size
    public File setSendFile(File file, int index, int size) {
        String content = file.getContent();
        if ((isFileCompleteInThisTransfering(file, index, size))) {
            content = content.substring(index);
        } else {
            content = content.substring(index, index + size);
        }
        File sendFile = new File(file.getFilename(), content);
        return sendFile;
    }

    public File setSendFile(File file, int index) {
        String content = file.getContent();
        content = content.substring(index);
        File sendFile = new File(file.getFilename(), content);
        return sendFile;
    }

    public File setSendFileWithoutT(File file, int index) {
        String content = file.getContent();
        content = content.substring(index);
        content = removeTLetter(content);
        File sendFile = new File(file.getFilename(), content);
        return sendFile;
    }

    public boolean isFileInList(File file, List<File> list) {
        String fileName = file.getFilename();
        for (File existingFile : list) {
            if (existingFile.getFilename().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public void sendingFile(File file, boolean isComplete) {
        // check if the file is in the sending list
        if (isComplete) {
            sendSchedule.remove(file);
        }
    }

    public void receivingFile(File file, boolean isComplete) {
        File incompleteFile = getFileInList(fileList, file);
        // add the file content
        incompleteFile.addContent(file.getContent());
        // update the file in the receiveSchedule list
        fileList.set(fileList.indexOf(incompleteFile), incompleteFile);
        if (isComplete) {
            // Recalculate the file size
            incompleteFile.setSize(incompleteFile.getContent().length());
        }
    }

    public boolean isFileCompleteInThisTransfering(File file, int index, int size) {
        if (index + size >= file.getSize()) {
            return true;
        }
        return false;
    }

    public void stopSendingFile(File file) {
        sendSchedule.remove(file);
    }

    public void stopReceivingFile(File file) {
        removeFile(file.getFilename());
    }

    public void removeTLetter(File file) {
        String content = file.getContent();
        // remove the t letter in the file content
        file.setContent(removeTLetter(content));
        file.setSize(removeTLetter(content).length());
        // update the file in the fileList
        for (File existingFile : fileList) {
            if (existingFile.getFilename().equals(file.getFilename())) {
                fileList.set(fileList.indexOf(existingFile), file);
                break;
            }
        }
    }

    private String removeTLetter(String content) {
        // remove the t letter in the file content
        content = content.replace("t", "");
        return content;
    }
}
