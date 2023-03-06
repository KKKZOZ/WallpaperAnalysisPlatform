package org.jff.vo;

import lombok.*;
import org.jff.Entity.Article;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {
    private Long articleId;
    private String title;
    private String content;
    private LocalDateTime publishTime;

    private Integer likeCount;
    private Integer dislikeCount;

    private PublisherVO publisherInfo;
    private int likeStatus;
    private List<CommentVO> commentList;

    public ArticleVO(Article article){
        this.articleId = article.getArticleId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.publishTime = article.getPublishTime();
        this.likeCount = article.getLikeCount();
        this.dislikeCount = article.getDislikeCount();
    }
    public ArticleVO(Article article,
                     PublisherVO publisherVO,
                     List<CommentVO> commentList,
                     int likeStatus){
        this.articleId = article.getArticleId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.publishTime = article.getPublishTime();
        this.likeCount = article.getLikeCount();
        this.dislikeCount = article.getDislikeCount();
        this.publisherInfo = publisherVO;
        this.commentList = commentList;
        this.likeStatus = likeStatus;
    }
}


