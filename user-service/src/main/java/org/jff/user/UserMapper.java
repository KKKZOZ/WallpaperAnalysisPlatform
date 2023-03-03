package org.jff.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


import java.util.Optional;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    default Optional findByUsername(String username){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return Optional.ofNullable(selectOne(wrapper));
    }
}
