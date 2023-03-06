package org.jff.client;

import org.jff.Entity.Article;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("article-service")
public interface ArticleServiceClient {
    @GetMapping("/api/v1/article/publisherId")
    public Long getPublisherIdByArticleId(@RequestParam("articleId") Long articleId);

    @GetMapping("/api/v1/article/info")
    public Article getArticleInfoByArticleId(@RequestParam("articleId") Long articleId);
}
