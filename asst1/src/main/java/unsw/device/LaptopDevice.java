package unsw.device;

import unsw.satellite.Satellite;
import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 100000.0);
    }

    @Override
    public boolean communicable(Satellite satellite) {
        return isCommunicable(satellite);
    }
}
