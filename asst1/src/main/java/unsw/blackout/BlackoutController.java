package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.device.*;
import unsw.satellite.*;
import unsw.file.*;
import unsw.transmission.*;

public class BlackoutController {
    private List<Device> devicesList = new ArrayList<>();
    private List<Satellite> satellitesList = new ArrayList<>();

    public void createDevice(String deviceId, String type, Angle position) {
        if (type.equals("HandheldDevice")) {
            devicesList.add(new HandheldDevice(deviceId, type, position));
        } else if (type.equals("LaptopDevice")) {
            devicesList.add(new LaptopDevice(deviceId, type, position));
        } else if (type.equals("DesktopDevice")) {
            devicesList.add(new DesktopDevice(deviceId, type, position));
        }
    }

    public void removeDevice(String deviceId) {
        Device device = findDevice(deviceId);
        if (device != null) {
            devicesList.remove(device);
        }
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        if (type.equals("StandardSatellite")) {
            satellitesList.add(new StandardSatellite(satelliteId, type, height, position));
        } else if (type.equals("TeleportingSatellite")) {
            satellitesList.add(new TeleportingSatellite(satelliteId, type, height, position));
        } else if (type.equals("RelaySatellite")) {
            satellitesList.add(new RelaySatellite(satelliteId, type, height, position));
        }
    }

    public void removeSatellite(String satelliteId) {
        Satellite satellite = findSatellite(satelliteId);
        if (satellite != null) {
            satellitesList.remove(satellite);
        }
    }

    public List<String> listDeviceIds() {
        return devicesList.stream().map(Device::getDeviceId).collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return satellitesList.stream().map(Satellite::getSatelliteId).collect(Collectors.toList());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        if (filename == null || content == null) {
            return;
        }
        File file = new File(filename, content);
        // check if the device exists
        Device device = findDevice(deviceId);
        if (device != null && isFileUnique(filename)) {
            // add the file to the device
            device.setFile(file);
            return;
        }
    }

    // Get detailed information about a single device or a satellite.
    public EntityInfoResponse getInfo(String id) {
        // get the device or satellite
        Device device = findDevice(id);
        if (device != null) {
            return new EntityInfoResponse(id, device.getPosition(), device.getHeight(), device.getType(),
                    device.getFileInfoMap());
        }

        Satellite satellite = findSatellite(id);
        if (satellite != null) {
            if (satellite instanceof RelaySatellite) {
                return new EntityInfoResponse(id, satellite.getPosition(), satellite.getHeight(), satellite.getType());
            }
            return new EntityInfoResponse(id, satellite.getPosition(), satellite.getHeight(), satellite.getType(),
                    satellite.getFileInfoMap());
        }
        return null;
    }

    public void simulate() {
        for (Satellite satellite : satellitesList) {
            satellite.move();
        }
        for (Device device : devicesList) {
            sendingFiles(device.getDeviceId());
        }
        for (Satellite satellite : satellitesList) {
            sendingFiles(satellite.getSatelliteId());
        }
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        // for devices
        Device device = findDevice(id);
        if (device != null) {
            return communicableForDevice(device);
        }
        // for satellites
        Satellite satellite = findSatellite(id);
        if (satellite != null) {
            return communicableForSatellite(satellite);
        }
        return null;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        File file = from.getFile(fileName);
        if (file == null) {
            throw new VirtualFileNotFoundException(fileName);
        }
        // File doesn't exist on fromId or it's a partial file (hasn't finished
        // transferring)
        if (from == null || (from.isFileInList(file, from.getFileList()) && !from.isFileComplete(file))) {
            throw new VirtualFileNotFoundException(fileName);
        }
        // Satellite Bandwidth is full
        if (!hasBandwidth(from, to)) {
            throw new VirtualFileNoBandwidthException(fileName);
        }
        // File already exists on toId or is currently downloading to the target
        if (to == null || to.isFileInList(file, to.getFileList())) {
            throw new VirtualFileAlreadyExistsException(fileName);
        }
        // No room on the receiver
        if (!to.hasRoom(file)) {
            throw new VirtualFileNoStorageSpaceException(fileName);
        }

        from.addFileToSendSchedule(file, toId);
        to.addFileInfo(file);

    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

    // ===================================================================================================
    // help functions

    // check if the device exists
    private Device findDevice(String deviceId) {
        for (Device device : devicesList) {
            if (device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    // check if the satellite exists
    private Satellite findSatellite(String satelliteId) {
        for (Satellite satellite : satellitesList) {
            if (satellite.getSatelliteId().equals(satelliteId)) {
                return satellite;
            }
        }
        return null;
    }

    private boolean isFileUnique(String filename) {
        for (Device device : devicesList) {
            if (device.getFileInfoMap().containsKey(filename)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRelaySatellite(String satelliteId) {
        Satellite satellite = findSatellite(satelliteId);
        if (satellite != null && satellite.getType().equals("RelaySatellite")) {
            return true;
        }
        return false;
    }

    // check if the device is in the range of the relay satellite
    private List<String> relaySatelliteCondition(Satellite satellite, List<String> communicableList) {
        // If there are new satellites in the satellite range (not
        // communicableList), add the new satellite to communicableList
        // If the new satellite is still a RelaySatellite, continue recursion
        List<String> newCommunicableList = new ArrayList<>();
        for (Satellite newSatellite : satellitesList) {
            if (newSatellite.communicable(satellite) && !communicableList.contains(newSatellite.getSatelliteId())) {
                newCommunicableList.add(newSatellite.getSatelliteId());
                if (isRelaySatellite(newSatellite.getSatelliteId())) {
                    newCommunicableList.addAll(relaySatelliteCondition(newSatellite, newCommunicableList));
                }
            }
        }
        return newCommunicableList;
    }

    // for device, create communication list between the satellites
    private List<String> communicableForDevice(Device device) {
        List<String> communicableList = new ArrayList<String>();
        if (satellitesList.isEmpty()) {
            return communicableList;
        }
        for (Satellite otherSatellite : satellitesList) {
            if (device.communicable(otherSatellite)) {
                communicableList.add(otherSatellite.getSatelliteId());
                if (isRelaySatellite(otherSatellite.getSatelliteId())) {
                    communicableList.addAll(relaySatelliteCondition(otherSatellite, communicableList));
                }
            }
        }
        return communicableList;
    }

    // for satellite, create communication list between the satellites and devices
    private List<String> communicableForSatellite(Satellite satellite) {
        List<String> communicableList = new ArrayList<String>();
        // find the satellites in the range of the satellite
        for (Satellite otherSatellite : satellitesList) {
            if (otherSatellite.equals(satellite)) {
                continue;
            }
            if (satellite.communicable(otherSatellite)) {
                communicableList.add(otherSatellite.getSatelliteId());
                if (isRelaySatellite(otherSatellite.getSatelliteId())) {
                    communicableList.addAll(relaySatelliteCondition(otherSatellite, communicableList));
                }
            }
        }
        // all satellites in the range of the satellite are added to the list
        // find the devices in the range of all the RelaySatellite in list, if this
        // device is not in the list, add it to the list
        for (Satellite otherSatellite : satellitesList) {
            if (communicableList.contains(otherSatellite.getSatelliteId())
                    && isRelaySatellite(otherSatellite.getSatelliteId()) || otherSatellite.equals(satellite)) {
                for (Device otherDevice : devicesList) {
                    if (otherSatellite.communicable(otherDevice)
                            && !communicableList.contains(otherDevice.getDeviceId())) {
                        communicableList.add(otherDevice.getDeviceId());
                    }
                }
            }
        }
        return communicableList;
    }

    private Sender getSender(String id) {
        return findDevice(id) != null ? findDevice(id) : findSatellite(id);
    }

    private Receiver getReceiver(String id) {
        return findDevice(id) != null ? findDevice(id) : findSatellite(id);
    }

    private boolean hasBandwidth(Sender from, Receiver to) {
        return from.hasBandwidthSend() && to.hasBandwidthReceive();
    }

    private void sendingFiles(String fromId) {
        Sender from = getSender(fromId);
        // get sendSchedule
        Map<File, String> sendSchedule = from.getSendSchedule();
        if (sendSchedule.isEmpty()) {
            return;
        }
        // send file in the sendSchedule, if from can communicate with to
        for (Map.Entry<File, String> entry : sendSchedule.entrySet()) {
            File file = entry.getKey();
            String toId = entry.getValue();
            if (canCommunicable(fromId, toId)) {
                sendingFile(fromId, file, toId);
            } else {
                Receiver to = getReceiver(toId);
                from.stopSendingFile(file);
                to.stopReceivingFile(file);
            }
        }
    }

    private void sendingFile(String fromId, File file, String toId) {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        if (from == null || to == null || file == null) {
            // Handle error or throw exception
            return;
        }
        int speed = getTransferSpeed(fromId, toId);
        File fileToSend;
        boolean isComplete = false;
        int index = to.getCurrentFileSize(file);
        fileToSend = from.setSendFile(file, index, speed);
        if (to.isFileCompleteInThisTransfering(file, index, speed)) {
            isComplete = true;
        }
        if (fileToSend == null) {
            // Handle error or throw exception
            return;
        }
        from.sendingFile(file, isComplete);
        to.receivingFile(fileToSend);
    }

    private int getTransferSpeed(String fromId, String toId) {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        int sSpeed = from.getSendSpeed();
        int rSpeed = to.getReceiveSpeed();
        if (sSpeed != -1 && rSpeed != -1) {
            return Math.min(sSpeed, rSpeed);
        } else if (sSpeed == -1) {
            return rSpeed;
        } else {
            return sSpeed;
        }
    }

    private boolean canCommunicable(String fromId, String toId) {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        if (from == null || to == null) {
            // Handle error or throw exception
            return false;
        }
        // 如果from是device，to是satellite
        if (from instanceof Device && to instanceof Satellite) {
            Device fromDevice = (Device) from;
            Satellite toSatellite = (Satellite) to;
            return fromDevice.communicable(toSatellite);
        } else if (from instanceof Satellite && to instanceof Device) {
            // 如果from是satellite，to是device
            Satellite fromSatellite = (Satellite) from;
            Device toDevice = (Device) to;
            return fromSatellite.communicable(toDevice);
        } else if (from instanceof Satellite && to instanceof Satellite) {
            // 如果from是satellite，to是satellite
            Satellite fromSatellite = (Satellite) from;
            Satellite toSatellite = (Satellite) to;
            return fromSatellite.communicable(toSatellite);
        } else {
            // Handle error or throw exception
            return false;
        }
    }

    public String getSendSchedule(String id) {
        Device device = findDevice(id);
        if (device == null) {
            return null;
        }
        return device.getSendSchedule().toString();
    }
}
