package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Set extends LikeableObject{

    @TableId(type = IdType.AUTO)
    private Long setId;
    private String setName;
    private Long userId;
    private LocalDateTime createTime;
    private boolean isPublic;

    private String coverUrl;

    public Set(){
        this.setLikeCount(0);
        this.setDislikeCount(0);
    }



}
