package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article extends LikeableObject{

    @TableId(type = IdType.AUTO)
    private Long articleId;
    private String title;
    private long publisherId;
    private String content;
    private LocalDateTime publishTime;

    public Article(Article article) {
        this.articleId = article.getArticleId();
        this.title = article.getTitle();
        this.publisherId = article.getPublisherId();
        this.content = article.getContent();
        this.publishTime = article.getPublishTime();
        this.setLikeCount(article.getLikeCount());
        this.setDislikeCount(article.getDislikeCount());
    }
}
