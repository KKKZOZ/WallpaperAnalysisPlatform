package org.jff.dto;

import lombok.Data;

@Data
public class SetDTO {

    private Long setId;
    private String setName;

    private boolean isPublic;
    private String coverUrl;

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }
}