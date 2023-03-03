package org.jff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jff.Entity.Set;

import java.util.Optional;

@Mapper
public interface SetMapper extends BaseMapper<Set>{
    default Optional<Set> getSetById(Long setId){
        return Optional.ofNullable(selectById(setId));
    }

}
