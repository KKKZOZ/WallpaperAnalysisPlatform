package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("wallpaper_comment")
public class SteamComment {

    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long wallpaperId;
    private String username;
    private String createTime;
    private String content;
}
