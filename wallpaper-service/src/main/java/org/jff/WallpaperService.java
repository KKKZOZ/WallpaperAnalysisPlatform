package org.jff;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.SteamComment;
import org.jff.Entity.Wallpaper;
import org.jff.vo.WallpaperDetails;
import org.jff.vo.WallpaperVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WallpaperService {
    private final WallpaperMapper wallpaperMapper;

    private final SteamCommentMapper steamCommentMapper;

    public List<WallpaperVO> getWallpaperVOListBySetId(Long setId) {
        return wallpaperMapper.getWallpaperVOListBySetId(setId);
    }

    public WallpaperDetails getWallpaperById(Long wallpaperId) {
        Wallpaper wallpaper = wallpaperMapper.selectById(wallpaperId);
        WallpaperDetails wallpaperDetails = new WallpaperDetails(wallpaper);
        LambdaQueryWrapper<SteamComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SteamComment::getWallpaperId, wallpaperId);
        List<SteamComment> list = steamCommentMapper.selectList(wrapper);
        wallpaperDetails.setCommentList(list);

        return wallpaperDetails;
    }

    public List<SteamComment> getCommentListByWallpaperId(Long wallpaperId) {
        LambdaQueryWrapper<SteamComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SteamComment::getWallpaperId, wallpaperId);
        List<SteamComment> list = steamCommentMapper.selectList(wrapper);
        return list;
    }
}
