package unsw.transmission.communicationservice;

import java.util.ArrayList;
import java.util.List;

import unsw.equipment.EquipmentInfo;
import unsw.equipment.device.Device;
import unsw.equipment.satellite.Satellite;

public class CommunicationService {
    private CommunicationServiceWorldStorage worldStorage;
    private List<Device> devicesList = new ArrayList<>();
    private List<Satellite> satellitesList = new ArrayList<>();

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
        for (Satellite otherSatellite : satellitesList) {
            EquipmentInfo info = otherSatellite.getInfo();
            if (device.communicable(info.getHeight(), info.getPosition(), info.getType())) {
                communicableList.add(otherSatellite.getSatelliteId());
                if (isRelaySatellite(otherSatellite.getSatelliteId())) {
                    communicableList.addAll(relaySatelliteCondition(otherSatellite, communicableList));
                }
            }
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
            if (satellite.communicable(info.getHeight(), info.getPosition())) {
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
                    EquipmentInfo info = otherDevice.getInfo();
                    if (otherSatellite.communicable(info.getPosition(), info.getType())
                            && !communicableList.contains(otherDevice.getDeviceId())) {
                        communicableList.add(otherDevice.getDeviceId());
                    }
                }
            }
        }
        return communicableList;
    }

    private boolean isRelaySatellite(String satelliteId) {
        Satellite satellite = worldStorage.getSatellite(satelliteId);
        EquipmentInfo info = satellite.getInfo();
        if (satellite != null && info.getType().equals("RelaySatellite")) {
            return true;
        }
        return false;
    }

    // check if the device is in the range of the relay satellite
    private List<String> relaySatelliteCondition(Satellite satellite, List<String> communicableList) {
        // If there are new satellites in the satellite range (not
        // communicableList), add the new satellite to communicableList
        // If the new satellite is still a RelaySatellite, continue recursion
        List<String> communicableEntitiesList = new ArrayList<>();
        for (Satellite newSatellite : satellitesList) {
            EquipmentInfo info = newSatellite.getInfo();
            if (newSatellite.communicable(info.getPosition(), info.getType())
                    && !communicableList.contains(newSatellite.getSatelliteId())) {
                communicableEntitiesList.add(newSatellite.getSatelliteId());
                if (isRelaySatellite(newSatellite.getSatelliteId())) {
                    communicableEntitiesList.addAll(relaySatelliteCondition(newSatellite, communicableEntitiesList));
                }
            }
        }
        return communicableEntitiesList;
    }
}
