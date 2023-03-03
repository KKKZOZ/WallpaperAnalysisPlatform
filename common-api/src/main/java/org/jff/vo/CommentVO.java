package org.jff.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentVO {

    private Long commentId;
    private Long setId;
    private Long articleId;
    private LocalDateTime publishTime;

    private PublisherVO publisherInfo;
    private String content;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer likeStatus;


}
