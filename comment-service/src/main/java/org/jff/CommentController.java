package org.jff;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.CommentType;
import org.jff.dto.CommentLikeStatus;
import org.jff.global.NotResponseBody;
import org.jff.global.ResponseVO;
import org.jff.vo.CommentVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/article")
    // 用户评论文章
    public ResponseVO addCommentToArticle(@RequestHeader("userId") Long userId,
                                          @RequestBody Map<String, String> map) {
        Long articleId = Long.parseLong(map.get("articleId"));
        String content = map.get("content");
        return commentService.addCommentToArticle(userId, articleId, content);
    }

    @DeleteMapping("/article")
    // 用户删除评论
    public ResponseVO deleteCommentFromArticle(@RequestHeader("userId") Long userId,
                                               @RequestParam("commentId") Long commentId) {
        return commentService.deleteComment(userId, commentId);
    }

    @PostMapping("/set")
    // 用户对set进行评论
    public ResponseVO addCommentToSet(@RequestHeader("userId") Long userId,
                                      @RequestBody Map<String, String> map) {
        Long setId = Long.parseLong(map.get("setId"));
        String content = map.get("content");
        return commentService.addCommentToSet(userId, setId, content);
    }

    @DeleteMapping("/set")
    // 用户删除评论
    public ResponseVO deleteCommentFromSet(@RequestHeader("userId") Long userId,
                                           @RequestParam("commentId") Long commentId) {
        return commentService.deleteComment(userId, commentId);
    }


    @PostMapping("/likeStatus")
    // 用户进行点赞或者取消点赞
    public ResponseVO changeLikeStatus(@RequestHeader("userId") Long userId,
                                       @RequestBody CommentLikeStatus likeStatus) {
        return commentService.changeLikeStatus(userId, likeStatus);
    }

    @GetMapping()
    @NotResponseBody("")
    // 获取文章的评论列表
    // 此接口为服务内部调用
    public List<CommentVO> getCommentListByArticleId(@RequestParam Long userId,
                                                     @RequestParam Long objectId,
                                                     @RequestParam CommentType type) {
        return commentService.getCommentListByObjectId(userId, objectId,type);
    }
}
