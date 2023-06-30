package unsw.equipment.satellite;

import java.util.List;
import java.util.Map;

import unsw.file.*;
import unsw.transmission.*;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public abstract class SatelliteHandlesFiles extends Satellite implements EntityInfo, Sender, Receiver {
    private FileHandler fileHandler;

    public SatelliteHandlesFiles(String satelliteId, String type, Double height, Angle position, Double speed,
            int direction, Double range, int maxFiles, int maxFileSize, int maxReceive, int maxSend) {
        super(satelliteId, type, height, position, speed, direction, range, maxFiles, maxFileSize, maxReceive, maxSend);
        this.fileHandler = new FileHandler();
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public List<File> getFileList() {
        return fileHandler.getFileList();
    }

    public EntityInfoResponse getEntityInfo() {
        if (fileHandler.isFileListEmpty()) {
            return new EntityInfoResponse(getSatelliteId(), getPosition(), getHeight(), getType());
        }
        return new EntityInfoResponse(getSatelliteId(), getPosition(), getHeight(), getType(), getFileInfoMap());
    }

    public Map<String, FileInfoResponse> getFileInfoMap() {
        return fileHandler.getFileInfoMap();
    }

    public Map<File, String> getSendSchedule() {
        return fileHandler.getSendSchedule();
    }

    public boolean isFileInList(File file, List<File> list) {
        return fileHandler.isFileInList(file, list);
    }

    public boolean isFileSending(File file) {
        return fileHandler.isFileSending(file);
    }

    public boolean isFileComplete(File file) {
        return fileHandler.isFileComplete(file);
    }

    public boolean isFileCompleteInThisTransfering(File file, int index, int size) {
        return fileHandler.isFileCompleteInThisTransfering(file, index, size);
    }

    public void addFileInfo(File file) {
        fileHandler.addFileInfo(file);
    }

    public void sendingFile(File file, boolean isComplete) {
        fileHandler.sendingFile(file, isComplete);
    }

    public void stopSendingFile(File file) {
        fileHandler.stopSendingFile(file);
    }

    public void receivingFile(File file, boolean isComplete) {
        fileHandler.receivingFile(file, isComplete);
    }

    public void stopReceivingFile(File file) {
        fileHandler.stopReceivingFile(file);
    }

    public void addFileToSendSchedule(File file, String id) {
        fileHandler.addFileToSendSchedule(file, id);
    }

    public int getCurrentFileSize(File file) {
        return fileHandler.getCurrentFileSize(file);
    }

    public File getFile(String fileName) {
        return fileHandler.getFile(fileName);
    }

    public File setSendFile(File file, int index, int size) {
        return fileHandler.setSendFile(file, index, size);
    }

    public File setSendFile(File file, int index) {
        return fileHandler.setSendFile(file, index);
    }

    public File setSendFileWithoutT(File file, int index) {
        return fileHandler.setSendFileWithoutT(file, index);
    }

    public void removeTLetter(File file) {
        fileHandler.removeTLetter(file);
    }

    // for sending and receiving
    public boolean hasBandwidthSend() {
        if (getMaxSendSpeed() > fileHandler.getSendScheduleSize()) {
            return true;
        }
        return false;
    }

    public boolean hasBandwidthReceive() {
        if (getMaxReceiveSpeed() > fileHandler.getReceivingSize()) {
            return true;
        }
        return false;
    }

    // + means still has space
    // - means no space
    public int getAvailableFileListSize() {
        return getMaxFiles() - fileHandler.getAllFilesSize();
    }

    public int getAvailableContentSize() {
        return getMaxFileSize() - fileHandler.getAllContentSize();
    }

    public boolean hasRoom(File file) {
        if (getMaxFiles() == -1 && getMaxFileSize() == -1) {
            return true;
        } else if (getMaxFiles() == -1 && getMaxFileSize() != -1) {
            if (getAvailableContentSize() >= fileHandler.getFileSize(file)) {
                return true;
            }
            // else if (getAvailableContentSize() < fileHandler.getFileSize(file)) {
            // int needSize = fileHandler.getFileSize(file) - getAvailableContentSize();
            // return fileHandler.removeFileFromTempFileList(needSize);
            // }
        } else if (getMaxFiles() != -1 && getMaxFileSize() == -1) {
            return getAvailableFileListSize() > 0;
        } else if (getMaxFiles() != -1 && getMaxFileSize() != -1) {
            return getAvailableFileListSize() > 0 && getAvailableContentSize() >= fileHandler.getFileSize(file);
        }
        return false;
    }

    public int getSendSpeed() {
        return getMaxSendSpeed() / fileHandler.getSendScheduleSize();
    }

    public int getReceiveSpeed() {
        return getMaxReceiveSpeed() / fileHandler.getReceivingSize();
    }

    public boolean isTeleport() {
        return false;
    }

    // public void moveFileToTempFileList(String fromId) {
    // fileHandler.moveFileToTempFileList(fromId);
    // }

    // public Map<File, String> getTempFile() {
    // return fileHandler.getTempFile();
    // }

    // public void moveFileToFileList(File file) {
    // fileHandler.moveFileToFileList(file);
    // }
}
