package org.jff;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jff.Entity.Article;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
