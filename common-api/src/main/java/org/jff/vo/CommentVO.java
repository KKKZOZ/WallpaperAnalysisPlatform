package org.jff.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jff.Entity.Comment;

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
    private boolean isDeletable;


    public CommentVO(Comment comment){
        this.commentId = comment.getCommentId();
        this.publishTime = comment.getPublishTime();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.dislikeCount = comment.getDislikeCount();
    }


}
