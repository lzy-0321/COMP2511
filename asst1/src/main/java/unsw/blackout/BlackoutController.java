package unsw.blackout;

import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.worldstorage.WorldStorage;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.equipment.device.*;
import unsw.equipment.satellite.*;
import unsw.file.*;
import unsw.transmission.*;
import unsw.transmission.communicationservice.CommunicationService;
import unsw.transmission.sendingservice.SendingService;

public class BlackoutController {
    private WorldStorage worldStorage = new WorldStorage();

    public void createDevice(String deviceId, String type, Angle position) {
        Device device = DeviceFactory.createDevice(deviceId, type, position);
        worldStorage.addDevice(device);
    }

    public void removeDevice(String deviceId) {
        Device device = findDevice(deviceId);
        if (device != null) {
            worldStorage.removeDevice(device);
        }
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite = SatelliteFactory.createSatellite(satelliteId, type, height, position);
        worldStorage.addSatellite(satellite);
    }

    public void removeSatellite(String satelliteId) {
        Satellite satellite = findSatellite(satelliteId);
        if (satellite != null) {
            worldStorage.removeSatellite(satellite);
        }
    }

    public List<String> listDeviceIds() {
        return worldStorage.listDeviceIds();
    }

    public List<String> listSatelliteIds() {
        return worldStorage.listSatelliteIds();
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        if (filename == null || content == null) {
            return;
        }
        File file = new File(filename, content);
        // check if the device exists
        Device device = findDevice(deviceId);
        if (device != null) {
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
            return device.getEntityInfo();
        }

        Satellite satellite = findSatellite(id);
        if (satellite != null) {
            return satellite.getEntityInfo();
        }
        return null;
    }

    public void simulate() {
        for (Satellite satellite : worldStorage.getSatellitesList()) {
            satellite.move();
        }
        SendingService sendingService = new SendingService(worldStorage);
        for (Device device : worldStorage.getDevicesList()) {
            sendingService.sendingFiles(device.getDeviceId());
        }
        for (Satellite satellite : worldStorage.getSatellitesHandlesFilesList()) {
            sendingService.sendingFiles(satellite.getSatelliteId());
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
        Sender sender = getSender(id);
        CommunicationService communicationService = new CommunicationService(worldStorage);
        return communicationService.communicableEntitiesInRange(sender);
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        File file = from.getFile(fileName);
        if (from == null || to == null) {
            return;
        }
        // File doesn't exist on fromId or it's a partial file (hasn't finished
        // transferring)
        if (file == null || (from.isFileInList(file, from.getFileList()) && !from.isFileComplete(file))) {
            throw new VirtualFileNotFoundException(fileName);
        }
        // Satellite Bandwidth is full
        if (!hasBandwidth(from, to)) {
            throw new VirtualFileNoBandwidthException(fileName);
        }
        // File already exists on toId or is currently downloading to the target
        if (to.isFileInList(file, to.getFileList())) {
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
    // ===================================================================================================

    // check if the device exists
    private Device findDevice(String deviceId) {
        if (worldStorage.getDevice(deviceId) != null) {
            return worldStorage.getDevice(deviceId);
        }
        return null;
    }

    // check if the satellite exists
    private Satellite findSatellite(String satelliteId) {
        if (worldStorage.getSatellite(satelliteId) != null) {
            return worldStorage.getSatellite(satelliteId);
        }
        return null;
    }

    private Object findEntity(String id) {
        Device device = findDevice(id);
        if (device != null) {
            return device;
        }

        Satellite satellite = findSatellite(id);
        if (satellite instanceof SatelliteHandlesFiles) {
            return satellite;
        }

        return null;
    }

    private Sender getSender(String id) {
        Object entity = findEntity(id);
        return entity instanceof Sender ? (Sender) entity : null;
    }

    private Receiver getReceiver(String id) {
        Object entity = findEntity(id);
        return entity instanceof Receiver ? (Receiver) entity : null;
    }

    private boolean hasBandwidth(Sender from, Receiver to) {
        return from.hasBandwidthSend() && to.hasBandwidthReceive();
    }

}
