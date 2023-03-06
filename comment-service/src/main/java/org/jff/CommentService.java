package org.jff;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.*;
import org.jff.client.ArticleServiceClient;
import org.jff.client.SetServiceClient;
import org.jff.client.UserServiceClient;
import org.jff.dto.CommentLikeStatus;
import org.jff.global.ResponseVO;
import org.jff.global.ResultCode;
import org.jff.mapper.LikeStatusMapper;
import org.jff.test.OrderMessageProducer;
import org.jff.vo.CommentVO;
import org.jff.vo.LikeStatusVO;
import org.jff.vo.PublisherVO;
import org.jff.vo.UserVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    private final LikeStatusMapper likeStatusMapper;

    private final UserServiceClient userServiceClient;

    private final ArticleServiceClient articleServiceClient;

    private final SetServiceClient setServiceClient;

    private final OrderMessageProducer orderMessageProducer;

    public ResponseVO addCommentToArticle(Long userId, Long articleId, String content) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setPublisherId(userId);
        comment.setContent(content);
        comment.setPublishTime(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setDislikeCount(0);
        commentMapper.insert(comment);
        NotificationEvent event = new NotificationEvent();

        Article article = articleServiceClient.getArticleInfoByArticleId(articleId);
        if(article.getPublisherId()==userId)
            return new ResponseVO(ResultCode.SUCCESS);
        UserVO initiatorInfo = userServiceClient.getUserInfoList(List.of(userId)).get(0);
        TargetInfo targetInfo = TargetInfo.builder()
                .targetId(articleId)
                .targetName(article.getTitle())
                .targetUrl("URL").build();

        event.setRecipientId(article.getPublisherId());
        event.setType(NotificationEvent.COMMENT);
        event.setCategory(NotificationEvent.ARTICLE);
        event.setInitiatorInfo(initiatorInfo);
        event.setTargetInfo(targetInfo);
        event.setContent(content);
        orderMessageProducer.sendEvent(event);

        return new ResponseVO(ResultCode.SUCCESS);
    }

    public List<CommentVO> getCommentListByObjectId(Long userId, Long objectId, CommentType type) {
        // 先把该文章下所有评论都查出来
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (type == CommentType.ARTICLE)
            wrapper.eq(Comment::getArticleId, objectId);
        if (type == CommentType.SET)
            wrapper.eq(Comment::getSetId, objectId);

        wrapper.orderByDesc(Comment::getPublishTime);

        List<Comment> commentList = commentMapper.selectList(wrapper);
        List<Long> userIdList = commentList.stream()
                .map(Comment::getPublisherId).toList();
        List<PublisherVO> publisherVOList = userServiceClient
                .getUserInfoList(userIdList).stream()
                .map(PublisherVO::new).toList();
        Map<Long, PublisherVO> userVOMap = publisherVOList.stream()
                .distinct()
                .collect(Collectors.toMap(PublisherVO::getPublisherId, publisherVO -> publisherVO));

        // 再把所有评论的点赞情况查出来
        // 根据userId和commentIdList，获取点赞情况
        List<Long> commentIdList = commentList.stream()
                .map(Comment::getCommentId).toList();
        List<LikeStatusVO> likeStatusList = likeStatusMapper.getLikeStatusList(userId, commentIdList);
        Map<Long, LikeStatusVO> likeStatusMap = likeStatusList.stream()
                .collect(Collectors.toMap(LikeStatusVO::getCommentId, Function.identity()));

        // 组装
        // TODO:给CommentVO增加新的构造方法
        List<CommentVO> list = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO(comment);
            if (type == CommentType.SET)
                commentVO.setSetId(objectId);
            if (type == CommentType.ARTICLE)
                commentVO.setArticleId(objectId);
            commentVO.setLikeStatus(likeStatusMap.get(comment.getCommentId()).getStatus());
            commentVO.setPublisherInfo(userVOMap.get(comment.getPublisherId()));
            commentVO.setDeletable(comment.getPublisherId().equals(userId));

            list.add(commentVO);
        }
        return list;
    }

    public ResponseVO deleteComment(Long userId, Long commentId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getCommentId, commentId);
        Comment comment = commentMapper.selectOne(wrapper);
        if (comment == null) {
            return new ResponseVO(ResultCode.COMMENT_IS_NOT_FOUND);
        }
        if (!comment.getPublisherId().equals(userId)) {
            return new ResponseVO(ResultCode.AUTHORIZATION_ERROR);
        }
        commentMapper.deleteById(commentId);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO addCommentToSet(Long userId, Long setId, String content) {
        Comment comment = new Comment();
        comment.setSetId(setId);
        comment.setPublisherId(userId);
        comment.setContent(content);
        comment.setPublishTime(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setDislikeCount(0);
        commentMapper.insert(comment);

        Set set = setServiceClient.getSetInfoBySetId(setId);
        log.info("set: {}", set);
        if(set.getUserId().equals(userId))
            return new ResponseVO(ResultCode.SUCCESS);
        UserVO initiatorInfo = userServiceClient.getUserInfoList(List.of(userId)).get(0);
        TargetInfo targetInfo = TargetInfo.builder()
                .targetId(set.getSetId())
                .targetName(set.getSetName())
                .targetUrl("URL").build();
        // TODO: 确定set系列的url的构成

        NotificationEvent event = new NotificationEvent();
        event.setRecipientId(set.getUserId());
        event.setType(NotificationEvent.COMMENT);
        event.setCategory(NotificationEvent.SET);
        event.setInitiatorInfo(initiatorInfo);
        event.setTargetInfo(targetInfo);
        event.setContent(content);

        orderMessageProducer.sendEvent(event);
        return new ResponseVO(ResultCode.SUCCESS);

    }

    public ResponseVO changeLikeStatus(Long userId, CommentLikeStatus likeStatus) {
        LikeStatus newStatus = likeStatusMapper
                .getLikeStatusByUserIdAndObjectID(userId, likeStatus.getCommentId(), LikeStatusMapper.COMMENT);
        int oldStatus = newStatus.getStatus();
        int curStatus = likeStatus.getStatus();
        newStatus.setStatus(curStatus);
        likeStatusMapper.updateById(newStatus);

        Comment comment = commentMapper.selectById(likeStatus.getCommentId());
        comment.updateLikeStatus(oldStatus, curStatus);
        commentMapper.updateById(comment);
        return new ResponseVO(ResultCode.SUCCESS);
    }
}
