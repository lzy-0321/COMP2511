package unsw.transmission.communicationservice;

import java.util.ArrayList;
import java.util.List;

import unsw.equipment.EquipmentInfo;
import unsw.equipment.device.Device;
import unsw.equipment.satellite.Satellite;
import unsw.equipment.satellite.SatelliteHandlesFiles;
import unsw.transmission.Receiver;
import unsw.transmission.Sender;

public class CommunicationService {
    private CommunicationServiceWorldStorage worldStorage;
    private List<Device> devicesList;
    private List<Satellite> satellitesList;

    public CommunicationService(CommunicationServiceWorldStorage worldStorage) {
        this.worldStorage = worldStorage;
        this.devicesList = worldStorage.getDevicesList();
        this.satellitesList = worldStorage.getSatellitesList();
    }

    public List<String> communicableEntitiesInRange(Device device) {
        List<String> communicableList = new ArrayList<String>();
        if (satellitesList.isEmpty()) {
            return communicableList;
        }
        // find the satellites in the range of the device
        for (Satellite otherSatellite : satellitesList) {
            EquipmentInfo info = otherSatellite.getInfo();
            if (device.communicable(info.getHeight(), info.getPosition(), info.getType())) {
                communicableList.add(otherSatellite.getSatelliteId());
                if (isRelaySatellite(otherSatellite.getSatelliteId())) {
                    communicableList = findRelaySatellite(otherSatellite, communicableList);
                }
            }
        }

        // find the relay satellite in the range of the device
        // for each relay satellite, find the satellites in its range
        if (isRelaySatelliteInList(communicableList)) {
            communicableList = findInRelaySatelliteRange(communicableList);
        }
        return communicableList;
    }

    // for satellite, create communication list between the satellites and devices
    public List<String> communicableEntitiesInRange(Satellite satellite) {
        List<String> communicableList = new ArrayList<String>();
        // find the satellites in the range of the satellite
        for (Satellite otherSatellite : satellitesList) {
            if (otherSatellite.equals(satellite)) {
                continue;
            }
            EquipmentInfo info = otherSatellite.getInfo();
            if (satellite.communicable(info.getHeight(), info.getPosition())
                    && !communicableList.contains(otherSatellite.getSatelliteId())) {
                communicableList.add(otherSatellite.getSatelliteId());
                if (isRelaySatellite(otherSatellite.getSatelliteId())) {
                    communicableList = findRelaySatellite(otherSatellite, communicableList);
                }
            }
        }

        // find the devices in the range of the satellite
        for (Device otherDevice : devicesList) {
            EquipmentInfo info = otherDevice.getInfo();
            if (satellite.communicable(info.getPosition(), info.getType())
                    && !communicableList.contains(otherDevice.getDeviceId())) {
                communicableList.add(otherDevice.getDeviceId());
            }
        }

        // if there are relay satellites in the communicableList
        // for each relay satellite, find the satellites and devices in its range
        if (isRelaySatelliteInList(communicableList)) {
            communicableList = findInRelaySatelliteRange(satellite, communicableList);
        }
        return communicableList;
    }

    private boolean isRelaySatellite(String satelliteId) {
        Satellite satellite = worldStorage.getSatellite(satelliteId);
        if (satellite == null) {
            return false;
        }
        EquipmentInfo info = satellite.getInfo();
        if (info.getType().equals("RelaySatellite")) {
            return true;
        }
        return false;
    }

    private boolean isRelaySatelliteInList(List<String> communicableList) {
        for (String id : communicableList) {
            if (isRelaySatellite(id)) {
                return true;
            }
        }
        return false;
    }

    // find all relay satellites in the range of the satellite
    private List<String> findRelaySatellite(Satellite satellite, List<String> communicableList) {
        // If there are new satellites in the satellite range (not
        // communicableList), add the new satellite to communicableList
        // If the new satellite is still a RelaySatellite, continue recursion
        for (Satellite newSatellite : satellitesList) {
            EquipmentInfo info = newSatellite.getInfo();
            if (newSatellite.communicable(info.getPosition(), info.getType())
                    && !communicableList.contains(newSatellite.getSatelliteId())
                    && isRelaySatellite(newSatellite.getSatelliteId())) {
                communicableList.add(newSatellite.getSatelliteId());
                communicableList = findRelaySatellite(newSatellite, communicableList);
            }
        }
        return communicableList;
    }

    private List<String> findInRelaySatelliteRange(List<String> communicableList) {
        // Find all Relaysatellites in communicableList

        // Find all devices in the RelaySatellite range

        // If the device is not in communicableList, add communicableList
        List<String> relaySatelliteList = new ArrayList<String>();
        for (String id : communicableList) {
            if (isRelaySatellite(id)) {
                relaySatelliteList.add(id);
            }
        }
        for (String id : relaySatelliteList) {
            Satellite satellite = worldStorage.getSatellite(id);
            for (Satellite otherSatellite : satellitesList) {
                if (!otherSatellite.equals(satellite)) {
                    EquipmentInfo info = otherSatellite.getInfo();
                    if (satellite.communicable(info.getHeight(), info.getPosition())
                            && !communicableList.contains(otherSatellite.getSatelliteId())) {
                        communicableList.add(otherSatellite.getSatelliteId());
                    }
                }
            }
        }
        return communicableList;
    }

    private List<String> findInRelaySatelliteRange(Satellite satellite, List<String> communicableList) {
        // Find all Relaysatellites in communicableList

        // Find all devices and satellites in the RelaySatellite range

        // If the device is not in communicableList, add communicableList

        // If the satellite is not in communicableList and is not a satellite, add
        // communicableList
        List<String> relaySatelliteList = new ArrayList<String>();
        for (String id : communicableList) {
            if (isRelaySatellite(id)) {
                relaySatelliteList.add(id);
            }
        }
        for (String id : relaySatelliteList) {
            Satellite relaySatellite = worldStorage.getSatellite(id);
            for (Satellite otherSatellite : satellitesList) {
                if (!otherSatellite.equals(relaySatellite) && !otherSatellite.equals(satellite)) {
                    EquipmentInfo info = otherSatellite.getInfo();
                    if (relaySatellite.communicable(info.getHeight(), info.getPosition())
                            && !communicableList.contains(otherSatellite.getSatelliteId())) {
                        communicableList.add(otherSatellite.getSatelliteId());
                    }
                }
            }
            for (Device otherDevice : devicesList) {
                EquipmentInfo info = otherDevice.getInfo();
                if (relaySatellite.communicable(info.getPosition(), info.getType())
                        && !communicableList.contains(otherDevice.getDeviceId())) {
                    communicableList.add(otherDevice.getDeviceId());
                }
            }
        }
        return communicableList;
    }

    public boolean isCommunicableEntitiesInRange(String fromId, String toId) {
        Sender from = getSender(fromId);
        Receiver to = getReceiver(toId);
        if (from == null || to == null) {
            return false;
        }
        // Check whether toId is in the communicableList of fromId
        if (from instanceof Device) {
            Device device = (Device) from;
            List<String> communicableList = communicableEntitiesInRange(device);
            if (communicableList.contains(toId)) {
                return true;
            }
        } else if (from instanceof Satellite) {
            Satellite satellite = (Satellite) from;
            List<String> communicableList = communicableEntitiesInRange(satellite);
            if (communicableList.contains(toId)) {
                return true;
            }
        }
        return false;
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
