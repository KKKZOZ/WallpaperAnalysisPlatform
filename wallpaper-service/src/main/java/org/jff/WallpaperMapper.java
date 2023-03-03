package org.jff;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jff.Entity.Wallpaper;
import org.jff.vo.WallpaperVO;

import java.util.List;

@Mapper
public interface WallpaperMapper extends BaseMapper<Wallpaper> {

    List<WallpaperVO> getWallpaperVOListBySetId(Long setId);
}
