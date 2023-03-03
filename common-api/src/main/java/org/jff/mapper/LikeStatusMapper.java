package org.jff.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jff.Entity.LikeStatus;
import org.jff.vo.LikeStatusVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper
public interface LikeStatusMapper extends BaseMapper<LikeStatus> {

    final Integer SET = 1,ARTICLE = 2,COMMENT=3;

    default LikeStatus getLikeStatusByUserIdAndObjectID(Long userId, Long objectId,Integer mode){
        LambdaQueryWrapper<LikeStatus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LikeStatus::getUserId, userId);
        if(Objects.equals(mode, SET))
            wrapper.eq(LikeStatus::getSetId, objectId);
        if(Objects.equals(mode,ARTICLE))
            wrapper.eq(LikeStatus::getArticleId,objectId);
        if(Objects.equals(mode,COMMENT))
            wrapper.eq(LikeStatus::getCommentId,objectId);
        LikeStatus status = selectOne(wrapper);
        if(status==null){
            status = new LikeStatus();
            status.setStatus(0);
            status.setUserId(userId);
            if(Objects.equals(mode, SET))
                status.setSetId(objectId);
            if(Objects.equals(mode,ARTICLE))
                status.setArticleId(objectId);
            if(Objects.equals(mode,COMMENT))
                status.setCommentId(objectId);
            //TODO: 确定insert返回的是id
            Long id = Long.parseLong(String.valueOf(this.insert(status)));
            status.setStatusId(id);
        }
        return status;
    }

    default List<LikeStatusVO> getLikeStatusList(Long userId, List<Long> commentIdList){
        List<LikeStatusVO> list = new ArrayList<>();
        for(Long commentId:commentIdList){
            list.add(
                    new LikeStatusVO(commentId,
                            getLikeStatusByUserIdAndObjectID(userId,commentId,COMMENT).getStatus()));
        }
        return list;
    }
}
