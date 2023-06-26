package unsw.equipment.satellite;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;
import unsw.file.*;

public class TeleportingSatellite extends SatelliteHandlesFiles {
    private List<Integer> directionList = new ArrayList<Integer>();

    public TeleportingSatellite(String satelliteId, String type, Double height, Angle position) {
        // 1 for clockwise, -1 for anticlockwise
        // -1 for maxFiles means unlimited
        super(satelliteId, type, height, position, 1000.0, 1, 200000.0, -1, 200, 15, 10);
        directionList.add(1);
    }

    @Override
    public void move() {
        Double newPosition = calculateNewPosition();
        // depend on the speed of this satellite, calculate the increase angle in this
        // move
        Double angle = (this.getSpeed() * 180) / (this.getHeight() * Math.PI);
        // When the position of the satellite reaches θ = 180, the satellite teleports
        // to θ = 0 and changes direction
        // start by moving anticlockwise
        if ((newPosition - angle >= 180 && newPosition <= 180) || (newPosition - angle <= 180 && newPosition >= 180)) {
            this.setDirection(this.getDirection() * -1);
            newPosition = 0.0;
        }
        this.setPosition(Angle.fromDegrees(newPosition));
        directionList.add(this.getDirection());
    }

    @Override
    // Supports all devices
    public boolean communicable(Angle devicePosition, String deviceType) {
        return isCommunicable(devicePosition);
    }

    @Override
    public boolean communicable(Double otherheight, Angle otherposition) {
        return isCommunicable(otherheight, otherposition);
    }

    @Override
    public void stopSendingFile(File file) {
        this.getFileHandler().stopSendingFile(file);
        if (this.isTeleport()) {
            this.removeTInFileList(file);
        }
    }

    @Override
    public boolean stopReceivingFile(File file) {
        this.getFileHandler().stopReceivingFile(file);
        if (this.isTeleport()) {
            return false;
        }
        return true;
    }

    private boolean isTeleport() {
        // If the last two digits of the directionList are different, it is teleport
        // If the last two digits of the directionList are the same, then it is not
        int size = directionList.size();
        if (directionList.get(size - 1) == directionList.get(size - 2)) {
            return false;
        }
        return true;
    }
}
