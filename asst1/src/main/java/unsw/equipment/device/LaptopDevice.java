package unsw.equipment.device;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    public LaptopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 100000.0);
    }

    @Override
    public boolean communicable(Double satelliteHigh, Angle satellitePosition, String satelliteType) {
        return isCommunicable(satelliteHigh, satellitePosition);
    }
}
