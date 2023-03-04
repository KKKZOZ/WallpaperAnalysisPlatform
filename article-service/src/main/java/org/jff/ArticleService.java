package org.jff;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.Article;
import org.jff.Entity.CommentType;
import org.jff.Entity.LikeStatus;
import org.jff.Entity.SearchBy;
import org.jff.client.CommentServiceClient;
import org.jff.client.UserServiceClient;
import org.jff.dto.ArticleLikeStatus;
import org.jff.global.APIException;
import org.jff.global.ResponseVO;
import org.jff.global.ResultCode;
import org.jff.mapper.LikeStatusMapper;
import org.jff.vo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleMapper articleMapper;

    private final CommentServiceClient commentServiceClient;

    private final UserServiceClient userServiceClient;

    private final LikeStatusMapper likeStatusMapper;

    public ResponseVO createArticle(Article article) {
        article.setPublishTime(LocalDateTime.now());
        article.setLikeCount(0);
        article.setDislikeCount(0);
        articleMapper.insert(article);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ArticleVO getArticleById(Long userId,Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new APIException(ResultCode.ARTICLE_IS_NOT_FOUND);
        }
        List<CommentVO> commentList = commentServiceClient.getCommentListByObjectId(userId, articleId, CommentType.ARTICLE);
        int likeStatus = likeStatusMapper
                .getLikeStatusByUserIdAndObjectID(userId, articleId,LikeStatusMapper.ARTICLE).getStatus();

        UserVO userVO = userServiceClient.getUserInfoList(List.of(article.getPublisherId())).get(0);
        PublisherVO publisherVO = new PublisherVO(userVO);
        ArticleVO articleVO = new ArticleVO(article);
        articleVO.setPublisherInfo(publisherVO);
        articleVO.setCommentList(commentList);
        articleVO.setLikeStatus(likeStatus);
        return articleVO;
    }

    public ResponseVO deleteArticle(Long userId, Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return new ResponseVO(ResultCode.ARTICLE_IS_NOT_FOUND);
        }
        if (article.getPublisherId()!=userId){
            return new ResponseVO(ResultCode.AUTHORIZATION_ERROR);
        }
        articleMapper.deleteById(articleId);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO updateArticle(Long userId, Article newArticle) {
        Article article = articleMapper.selectById(newArticle.getArticleId());
        if (article == null) {
            return new ResponseVO(ResultCode.ARTICLE_IS_NOT_FOUND);
        }
        if (article.getPublisherId()!=userId){
            return new ResponseVO(ResultCode.AUTHORIZATION_ERROR);
        }
        articleMapper.updateById(newArticle);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public List<Article> getArticleListByUserId(Long userId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getPublisherId, userId);
        return articleMapper.selectList(queryWrapper);
    }

    public ResponseVO changeLikeStatus(Long userId, ArticleLikeStatus likeStatus) {
        LikeStatus newStatus = likeStatusMapper
                .getLikeStatusByUserIdAndObjectID(userId,likeStatus.getArticleId(),LikeStatusMapper.ARTICLE);
        int oldStatus = newStatus.getStatus();
        int curStatus = likeStatus.getStatus();
        newStatus.setStatus(curStatus);
        likeStatusMapper.updateById(newStatus);

        Article article = articleMapper.selectById(likeStatus.getArticleId());
        article.updateLikeStatus(oldStatus,curStatus);
        articleMapper.updateById(article);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ArticleVOList searchArticle(Long userId, String title, Integer pageNum, Integer pageSize, Integer condition) {
        // TODO:userID 没用到

        ArticleVOList articleVOList = new ArticleVOList();

        List<ArticleVO> list = new ArrayList<>();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (!title.equals(""))
            queryWrapper.like(Article::getTitle, title);
        // isLatest 1
        // isHottest 2
        if (condition == 1)
            queryWrapper.orderByDesc(Article::getPublishTime);
        if (condition == 2)
            queryWrapper.orderByDesc(Article::getLikeCount);

        Page<Article> page = new Page<>(pageNum, pageSize);

        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        log.info("articlePageTotal:{}", articlePage.getTotal());

        List<Article> articleList = articlePage.getRecords();

        List<Long> userIdList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleVO articleVO = new ArticleVO(article);
            userIdList.add(article.getPublisherId());
            list.add(articleVO);
        }
        log.info("userIdListSize:{}", userIdList.size());
        log.info("listSize:{}", list.size());
        List<UserVO> userInfoList = userServiceClient.getUserInfoList(userIdList);
        log.info("userInfoListSize:{}", userInfoList.size());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPublisherInfo(new PublisherVO(userInfoList.get(i)));
        }

        articleVOList.setList(list);
        articleVOList.setTotal(articlePage.getTotal());
        return articleVOList;
    }
}
