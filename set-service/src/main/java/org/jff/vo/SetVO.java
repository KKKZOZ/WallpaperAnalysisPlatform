package org.jff.vo;

import lombok.Data;
import org.jff.Entity.Set;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SetVO{

    private Long setId;
    private String setName;

    private LocalDateTime createTime;
    private Integer likeCount;
    private Integer dislikeCount;
    private String coverUrl;
    private int likeStatus;

    private PublisherVO publisherInfo;
    private List<WallpaperVO> wallpaperList;
    private List<CommentVO> commentList;

    public SetVO(Set set){
        this.setSetId(set.getSetId());
        this.setSetName(set.getSetName());
        this.setCreateTime(set.getCreateTime());
        this.setLikeCount(set.getLikeCount());
        this.setDislikeCount(set.getDislikeCount());
        this.setCoverUrl(set.getCoverUrl());
    }
}
