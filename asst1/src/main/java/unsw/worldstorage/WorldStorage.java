package unsw.worldstorage;

import java.util.ArrayList;
import java.util.List;

import unsw.equipment.device.Device;
import unsw.equipment.satellite.Satellite;
import unsw.equipment.satellite.SatelliteHandlesFiles;
import unsw.transmission.communicationservice.CommunicationServiceWorldStorage;
import unsw.transmission.sendingservice.SendingServiceWorldStorage;

public class WorldStorage implements CommunicationServiceWorldStorage, SendingServiceWorldStorage {
    private List<Device> devicesList;
    private List<Satellite> satellitesList;

    public WorldStorage() {
        this.devicesList = new ArrayList<>();
        this.satellitesList = new ArrayList<>();
    }

    public List<Device> getDevicesList() {
        return devicesList;
    }

    public List<Satellite> getSatellitesList() {
        return satellitesList;
    }

    public void addDevice(Device device) {
        devicesList.add(device);
    }

    public Device getDevice(String deviceId) {
        for (Device device : devicesList) {
            if (device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    public void removeDevice(Device device) {
        devicesList.remove(device);
    }

    public List<String> listDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        for (Device device : devicesList) {
            deviceIds.add(device.getDeviceId());
        }
        return deviceIds;
    }

    public void addSatellite(Satellite satellite) {
        satellitesList.add(satellite);
    }

    public void removeSatellite(Satellite satellite) {
        satellitesList.remove(satellite);
    }

    public List<String> listSatelliteIds() {
        List<String> satelliteIds = new ArrayList<>();
        for (Satellite satellite : satellitesList) {
            satelliteIds.add(satellite.getSatelliteId());
        }
        return satelliteIds;
    }

    public Satellite getSatellite(String satelliteId) {
        for (Satellite satellite : satellitesList) {
            if (satellite.getSatelliteId().equals(satelliteId)) {
                return satellite;
            }
        }
        return null;
    }

    public List<SatelliteHandlesFiles> getSatellitesHandlesFilesList() {
        List<SatelliteHandlesFiles> satellitesHandlesFilesList = new ArrayList<>();
        for (Satellite satellite : satellitesList) {
            if (satellite instanceof SatelliteHandlesFiles) {
                satellitesHandlesFilesList.add((SatelliteHandlesFiles) satellite);
            }
        }
        return satellitesHandlesFilesList;
    }
}
