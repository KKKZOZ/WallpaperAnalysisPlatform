package org.jff.client;

import org.jff.Entity.CommentType;
import org.jff.vo.CommentVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("comment-service")
public interface CommentServiceClient {

    @GetMapping("/api/v1/comment")
    public List<CommentVO> getCommentListByObjectId(@RequestParam Long userId,
                                                     @RequestParam Long objectId,
                                                     @RequestParam CommentType type);

}
