package org.jff.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jff.Entity.Set;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetInfoVO {

    private Long setId;
    private String setName;

    private boolean isPublic;
    private String coverUrl;

    public SetInfoVO(Set set) {
        this.setId = set.getSetId();
        this.setName = set.getSetName();
        this.isPublic = set.isPublic();
        this.coverUrl = set.getCoverUrl();
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public boolean getIsPublic() {
        return isPublic;
    }

    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
