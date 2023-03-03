package org.jff;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jff.Entity.Comment;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
