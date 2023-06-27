package unsw.transmission.sendingservice;

import java.util.Map;

import unsw.transmission.Receiver;
import unsw.transmission.Sender;
import unsw.equipment.device.*;
import unsw.equipment.satellite.*;
import unsw.file.*;
import unsw.transmission.communicationservice.CommunicationService;
import unsw.worldstorage.WorldStorage;

public class SendingService {
    private CommunicationService communicationService;
    private SendingServiceWorldStorage worldStorage;

    public SendingService(WorldStorage worldStorage) {
        this.worldStorage = worldStorage;
        this.communicationService = new CommunicationService(worldStorage);
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
            Receiver to = getReceiver(toId);
            // if from is a TeleportingSatellite and it is teleporting, then teleport the
            // file
            if (from instanceof TeleportingSatellite) {
                SatelliteHandlesFiles satelliteHandlesFiles = (SatelliteHandlesFiles) from;
                if (satelliteHandlesFiles.isTeleport()) {
                    teleportingFile(fromId, file, toId);
                    break;
                }
            } else if (to instanceof TeleportingSatellite) {
                SatelliteHandlesFiles satelliteHandlesFiles = (SatelliteHandlesFiles) to;
                if (satelliteHandlesFiles.isTeleport()) {
                    teleportingFile(fromId, file, toId);
                    break;
                }
            }
            if (communicationService.isCommunicableEntitiesInRange(fromId, toId)) {
                sendingFile(fromId, file, toId);
            } else {
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
        to.receivingFile(fileToSend, isComplete);
    }

    private void teleportingFile(String fromId, File file, String toId) {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        if (from == null || to == null || file == null) {
            return;
        }
        if (from instanceof TeleportingSatellite) {
            // send the rest of file without t letter
            int index = to.getCurrentFileSize(file);
            File fileToSend = from.setSendFileWithoutT(file, index);
            boolean isComplete = true;
            from.sendingFile(file, isComplete);
            to.receivingFile(fileToSend, isComplete);
        } else if (to instanceof TeleportingSatellite) {
            // remove the t letter in sender's file
            from.removeTLetter(file);
            from.stopSendingFile(file);
            to.stopReceivingFile(file);
        }
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

    private Object findEntity(String id) {
        Device device = worldStorage.getDevice(id);
        if (device != null) {
            return device;
        }

        Satellite satellite = worldStorage.getSatellite(id);
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
