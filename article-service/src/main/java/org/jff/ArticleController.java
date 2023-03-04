package org.jff;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.Article;
import org.jff.Entity.SearchBy;
import org.jff.dto.ArticleLikeStatus;
import org.jff.dto.SetLikeStatus;
import org.jff.global.ResponseVO;
import org.jff.global.ResultCode;
import org.jff.vo.ArticleVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/article")
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @GetMapping()
    // 根据文章id获取文章
    public ArticleVO getArticleById(@RequestHeader("userId") Long userId,
                                    @RequestParam("articleId") Long articleId){
        return articleService.getArticleById(userId,articleId);
    }

    @GetMapping("/all")
    // 根据用户id获取所有文章
    public List<Article> getArticleListByUserId(@RequestHeader("userId") Long userId){
        log.info("用户({})获取所有文章",userId);
        return articleService.getArticleListByUserId(userId);
    }

    @PostMapping()
    // 用户创建新文章
    public ResponseVO createArticle(@RequestHeader("userId") Long userId,
                                    @RequestBody Article article){
        log.info("userId: {}",userId);
        article.setPublisherId(userId);
        return articleService.createArticle(article);
    }

    @PutMapping()
    // 用户更新文章
    public ResponseVO updateArticle(@RequestHeader("userId") Long userId,
                                    @RequestBody Article article){
        log.info("userId: {}",userId);
        return articleService.updateArticle(userId, article);
    }

    @DeleteMapping()
    // 用户删除文章
    public ResponseVO deleteArticle(@RequestHeader("userId") Long userId,
                                    @RequestParam("articleId") Long articleId){
        log.info("userId: {}",userId);
        return articleService.deleteArticle(userId, articleId);
    }

    @GetMapping("/search")
    // 用户搜索文章
    public List<ArticleVO> searchArticle(@RequestHeader("userId") Long userId,
                                         @RequestParam("name") String name,
                                         @RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("condition") Integer condition){
        log.info("userId: {}",userId);
        log.info("condition: {}",condition);
        return articleService.searchArticle(userId, name, pageNum, pageSize, condition);
    }

    @PostMapping("/likeStatus")
    // 用户进行点赞或者取消点赞
    public ResponseVO changeLikeStatus(@RequestHeader("userId") Long userId,
                                       @RequestBody ArticleLikeStatus likeStatus){
        return articleService.changeLikeStatus(userId,likeStatus);
    }


}
