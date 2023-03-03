package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("wallpaper_set")
public class SetRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long setId;
    private Long wallpaperId;


}
