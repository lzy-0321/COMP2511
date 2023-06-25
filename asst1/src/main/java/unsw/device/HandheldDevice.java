package unsw.device;

import unsw.satellite.Satellite;
import unsw.utils.Angle;

public class HandheldDevice extends Device {
    public HandheldDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 50000.0);
    }

    @Override
    public boolean communicable(Satellite satellite) {
        return isCommunicable(satellite);
    }
}
