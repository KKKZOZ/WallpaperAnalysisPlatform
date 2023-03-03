package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("like_status")
public class LikeStatus {

    @TableId(type= IdType.AUTO)
    private Long statusId;
    private Long userId;
    private Long setId;
    private Long articleId;
    private Long commentId;
    private int status;

}
