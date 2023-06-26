package unsw.equipment;

import unsw.utils.Angle;

public class EquipmentInfo {
    private Double height;
    private Angle position;
    private String type;

    public EquipmentInfo(Double height, Angle position, String type) {
        this.height = height;
        this.position = position;
        this.type = type;
    }

    public Double getHeight() {
        return height;
    }

    public Angle getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }
}
