package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_comment")
public class Comment extends LikeableObject{

    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long setId;
    private Long articleId;
    private Long publisherId;
    private LocalDateTime publishTime;
    private String content;
}
