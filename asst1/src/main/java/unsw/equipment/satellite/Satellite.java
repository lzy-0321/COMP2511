package unsw.equipment.satellite;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;
import unsw.equipment.EquipmentInfo;
import unsw.response.models.EntityInfoResponse;

public abstract class Satellite {
    private String satelliteId;
    private String type;
    private Double height;
    private Angle position;
    private Double speed;
    private Double range;
    private int direction;
    private int maxFiles;
    private int maxFileSize;
    private int maxReceiveSpeed;
    private int maxSendSpeed;

    public Satellite(String satelliteId, String type, Double height, Angle position, Double speed, int direction,
            Double range, int maxFiles, int maxFileSize, int maxReceive, int maxSend) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
        this.speed = speed;
        this.direction = direction;
        this.range = range;
        this.maxFiles = maxFiles;
        this.maxFileSize = maxFileSize;
        this.maxReceiveSpeed = maxReceive;
        this.maxSendSpeed = maxSend;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public String getType() {
        return type;
    }

    public Double getHeight() {
        return height;
    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public EquipmentInfo getInfo() {
        return new EquipmentInfo(height, position, type);
    }

    public Double getSpeed() {
        return speed;
    }

    public Double getRange() {
        return range;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public int getMaxReceiveSpeed() {
        return maxReceiveSpeed;
    }

    public int getMaxSendSpeed() {
        return maxSendSpeed;
    }

    public abstract EntityInfoResponse getEntityInfo();

    // =============
    // for moving satellite
    public Double calculateNewPosition() {
        Double increase = (this.speed * 180) / (height * Math.PI); // in degrees
        Double oldPosition = position.toDegrees();
        Double newPosition = oldPosition + (direction * increase);
        // if the new position is greater than 360, then it is the same as subtracting
        // 360
        if (newPosition > 360) {
            newPosition = newPosition - 360;
        } else if (newPosition < 0) { // if the new position is less than 0, then it is the same as adding 360
            newPosition = newPosition + 360;
        }
        return newPosition;
    }

    public abstract void move();

    // ===============
    // for communicating
    // only calulates if the device is communicable by satellite, does not care
    // about the type of
    // device
    public boolean isCommunicable(Angle devicePosition) {
        Double satelliteHigh = height;
        Angle satellitePosition = position;
        Double distance = MathsHelper.getDistance(satelliteHigh, satellitePosition, devicePosition);
        Boolean isVisible = MathsHelper.isVisible(satelliteHigh, satellitePosition, devicePosition);
        Double range = this.getRange();

        if (isVisible && distance <= range) {
            return true;
        }
        return false;
    }

    public abstract boolean communicable(Angle devicePosition, String deviceType);

    public boolean isCommunicable(Double otherheight, Angle otherposition) {
        Double satelliteHigh = height;
        Angle satellitePosition = position;
        Double otherSatelliteHigh = otherheight;
        Angle otherSatellitePosition = otherposition;
        Double range = this.getRange();
        Double distance = MathsHelper.getDistance(satelliteHigh, satellitePosition, otherSatelliteHigh,
                otherSatellitePosition);
        Boolean isVisible = MathsHelper.isVisible(satelliteHigh, satellitePosition, otherSatelliteHigh,
                otherSatellitePosition);

        if (isVisible && distance <= range) {
            return true;
        }
        return false;
    }

    public abstract boolean communicable(Double otherheight, Angle otherposition);

}
