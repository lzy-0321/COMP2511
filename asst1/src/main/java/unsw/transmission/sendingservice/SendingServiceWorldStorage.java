package unsw.transmission.sendingservice;

import unsw.equipment.device.Device;
import unsw.equipment.satellite.Satellite;

public interface SendingServiceWorldStorage {
    Device getDevice(String deviceId);

    Satellite getSatellite(String satelliteId);
}
