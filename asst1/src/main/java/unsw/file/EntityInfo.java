package unsw.file;

import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;

public interface EntityInfo {
    Map<String, FileInfoResponse> getFileInfoMap();

    EntityInfoResponse getEntityInfo();

}
