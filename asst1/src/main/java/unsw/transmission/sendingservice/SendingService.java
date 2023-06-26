package unsw.transmission.sendingservice;

import java.util.Map;

import unsw.transmission.Receiver;
import unsw.transmission.Sender;
import unsw.equipment.EquipmentInfo;
import unsw.equipment.device.*;
import unsw.equipment.satellite.*;
import unsw.file.*;

public class SendingService {
    private SendingServiceWorldStorage worldStorage;

    public SendingService(SendingServiceWorldStorage worldStorage) {
        this.worldStorage = worldStorage;
    }

    public void sendingFiles(String fromId) {
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
                boolean isNotTeleport = to.stopReceivingFile(file);
                if (isNotTeleport) {
                    from.removeTInFileList(file);
                }
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
            return false;
        }
        // 如果from是device，to是satellite
        if (from instanceof Device && to instanceof Satellite) {
            Device fromDevice = (Device) from;
            Satellite toSatellite = (Satellite) to;
            EquipmentInfo info = toSatellite.getInfo();
            return fromDevice.communicable(info.getHeight(), info.getPosition(), info.getType());
        } else if (from instanceof Satellite && to instanceof Device) {
            // 如果from是satellite，to是device
            Satellite fromSatellite = (Satellite) from;
            Device toDevice = (Device) to;
            EquipmentInfo info = toDevice.getInfo();
            return fromSatellite.communicable(info.getPosition(), info.getType());
        } else if (from instanceof Satellite && to instanceof Satellite) {
            // 如果from是satellite，to是satellite
            Satellite fromSatellite = (Satellite) from;
            Satellite toSatellite = (Satellite) to;
            EquipmentInfo info = toSatellite.getInfo();
            return fromSatellite.communicable(info.getPosition(), info.getType());
        } else {
            return false;
        }
    }

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
}
