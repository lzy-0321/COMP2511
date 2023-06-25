package unsw.device;

import unsw.satellite.Satellite;
import unsw.utils.Angle;

public class DesktopDevice extends Device {
    public DesktopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 200000.0);
    }

    @Override
    public boolean communicable(Satellite satellite) {
        return isCommunicable(satellite);
    }
}
