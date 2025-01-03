package unsw.transmission.communicationservice;

import java.util.List;

import unsw.equipment.device.Device;
import unsw.equipment.satellite.Satellite;

public interface CommunicationServiceWorldStorage {
    List<Device> getDevicesList();

    List<Satellite> getSatellitesList();

    Device getDevice(String deviceId);

    Satellite getSatellite(String satelliteId);

}
