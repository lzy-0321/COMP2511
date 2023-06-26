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

    @Override
    public List<File> getFileList() {
        return fileHandler.getFileList();
    }

    public EntityInfoResponse getEntityInfo() {
        if (fileHandler.isFileListEmpty()) {
            return new EntityInfoResponse(getSatelliteId(), getPosition(), getHeight(), getType());
        }
        return new EntityInfoResponse(getSatelliteId(), getPosition(), getHeight(), getType(), getFileInfoMap());
    }

    @Override
    public Map<String, FileInfoResponse> getFileInfoMap() {
        return fileHandler.getFileInfoMap();
    }

    @Override
    public Map<File, String> getSendSchedule() {
        return fileHandler.getSendSchedule();
    }

    @Override
    public boolean isFileInList(File file, List<File> list) {
        return fileHandler.isFileInList(file, list);
    }

    @Override
    public boolean isFileSending(File file) {
        return fileHandler.isFileSending(file);
    }

    @Override
    public boolean isFileComplete(File file) {
        return fileHandler.isFileComplete(file);
    }

    @Override
    public boolean isFileCompleteInThisTransfering(File file, int index, int size) {
        return fileHandler.isFileCompleteInThisTransfering(file, index, size);
    }

    @Override
    public void addFileInfo(File file) {
        fileHandler.addFileInfo(file);
    }

    @Override
    public void sendingFile(File file, boolean isComplete) {
        fileHandler.sendingFile(file, isComplete);
    }

    @Override
    public void stopSendingFile(File file) {
        fileHandler.stopSendingFile(file);
    }

    @Override
    public void receivingFile(File file) {
        fileHandler.receivingFile(file);
    }

    @Override
    public boolean stopReceivingFile(File file) {
        fileHandler.stopReceivingFile(file);
        return true;
    }

    @Override
    public void addFileToSendSchedule(File file, String id) {
        fileHandler.addFileToSendSchedule(file, id);
    }

    @Override
    public int getCurrentFileSize(File file) {
        return fileHandler.getCurrentFileSize(file);
    }

    @Override
    public File getFile(String fileName) {
        return fileHandler.getFile(fileName);
    }

    @Override
    public File setSendFile(File file, int index, int size) {
        return fileHandler.setSendFile(file, index, size);
    }

    @Override
    public void removeTInFileList(File file) {
        fileHandler.removeTInFileList(file);
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
        if (getMaxFiles() == -1) {
            return 1;
        }
        return getMaxFiles() - fileHandler.getAllFilesSize();
    }

    public int getAvailableContentSize() {
        if (getMaxFileSize() == -1) {
            return 1;
        }
        return getMaxFileSize() - fileHandler.getAllContentSize();
    }

    public boolean hasRoom(File file) {
        if (getAvailableFileListSize() > 0 && getAvailableContentSize() > fileHandler.getFileSize(file)) {
            return true;
        }
        return false;
    }

    public int getSendSpeed() {
        return getMaxSendSpeed() / fileHandler.getSendScheduleSize();
    }

    public int getReceiveSpeed() {
        return getMaxReceiveSpeed() / fileHandler.getReceivingSize();
    }

}
