package unsw.device;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import unsw.file.*;
import unsw.transmission.*;
import unsw.satellite.*;

public abstract class Device extends FileHolder implements Sender, Receiver {
    private String deviceId;
    private Double height = MathsHelper.RADIUS_OF_JUPITER;
    private String type;
    private Angle position;
    private Double range;

    public Device(String deviceId, String type, Angle position, Double range) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
        this.range = range;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Double getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public Double getRange() {
        return this.range;
    }

    public boolean hasBandwidthSend() {
        return true;
    }

    public boolean hasBandwidthReceive() {
        return true;
    }

    public boolean hasRoom(File file) {
        return true;
    }

    // -1 means no limit
    public int getSendSpeed() {
        return -1;
    }

    public int getReceiveSpeed() {
        return -1;
    }

    public boolean isCommunicable(Satellite satellite) {
        Double satelliteHigh = satellite.getHeight();
        Angle satellitePosition = satellite.getPosition();
        Angle devicePosition = getPosition();
        Double distance = MathsHelper.getDistance(satelliteHigh, satellitePosition, devicePosition);
        Boolean isVisible = MathsHelper.isVisible(satelliteHigh, satellitePosition, devicePosition);
        Double range = getRange();

        if (isVisible && distance <= range) {
            return true;
        }
        return false;
    }

    public abstract boolean communicable(Satellite satellite);
}
