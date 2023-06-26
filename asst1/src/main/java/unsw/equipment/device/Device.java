package unsw.equipment.device;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import java.util.List;
import java.util.Map;

import unsw.equipment.EquipmentInfo;
import unsw.file.*;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.transmission.*;

public abstract class Device implements EntityInfo, Sender, Receiver {
    private FileHandler fileHandler;
    private String deviceId;
    private Double height = MathsHelper.RADIUS_OF_JUPITER;
    private String type;
    private Angle position;
    private Double range;

    public Device(String deviceId, String type, Angle position, Double range) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
        this.range = range;
        this.fileHandler = new FileHandler();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public EquipmentInfo getInfo() {
        return new EquipmentInfo(height, position, type);
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public Double getRange() {
        return this.range;
    }

    public boolean hasBandwidthSend() {
        return true;
    }

    public boolean hasBandwidthReceive() {
        return true;
    }

    public boolean hasRoom(File file) {
        return true;
    }

    // -1 means no limit
    public int getSendSpeed() {
        return -1;
    }

    public int getReceiveSpeed() {
        return -1;
    }

    public boolean isCommunicable(Double satelliteHigh, Angle satellitePosition) {
        Angle devicePosition = position;
        Double distance = MathsHelper.getDistance(satelliteHigh, satellitePosition, devicePosition);
        Boolean isVisible = MathsHelper.isVisible(satelliteHigh, satellitePosition, devicePosition);
        Double range = getRange();

        if (isVisible && distance <= range) {
            return true;
        }
        return false;
    }

    public abstract boolean communicable(Double satelliteHigh, Angle satellitePosition, String satelliteType);

    public void setFile(File file) {
        fileHandler.setFile(file);
    }

    @Override
    public List<File> getFileList() {
        return fileHandler.getFileList();
    }

    @Override
    public EntityInfoResponse getEntityInfo() {
        if (fileHandler.isFileListEmpty()) {
            return new EntityInfoResponse(this.deviceId, this.position, this.height, this.type);
        }
        return new EntityInfoResponse(this.deviceId, this.position, this.height, this.type, getFileInfoMap());
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
}
