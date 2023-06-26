package unsw.equipment.satellite;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public abstract class SatelliteDoesNotHandleFiles extends Satellite {
    // 不需要处理文件，所以不实现FileHolder接口
    public SatelliteDoesNotHandleFiles(String satelliteId, String type, Double height, Angle position, Double speed,
            int direction, Double range) {
        super(satelliteId, type, height, position, speed, direction, range, 0, 0, -1, -1);
    }

    @Override
    public EntityInfoResponse getEntityInfo() {
        return new EntityInfoResponse(getSatelliteId(), getPosition(), getHeight(), getType());
    }
}
