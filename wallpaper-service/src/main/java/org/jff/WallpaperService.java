package org.jff;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.SteamComment;
import org.jff.Entity.Wallpaper;
import org.jff.vo.WallpaperDetails;
import org.jff.vo.WallpaperListVO;
import org.jff.vo.WallpaperVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public WallpaperListVO searchWallpaper(
            String name, List<String> category,
            int pageNum, int pageSize,
            List<String> resolution,
            int condition) {
        WallpaperListVO wallpaperListVO = new WallpaperListVO();
        List<WallpaperVO> list = new ArrayList<>();
        LambdaQueryWrapper<Wallpaper> wrapper = new LambdaQueryWrapper<>();

        if (!name.equals(""))
            wrapper.like(Wallpaper::getName, name);
        if (category.size() > 0)
            wrapper.in(Wallpaper::getCategory, category);
        if(resolution.size()>0)
            wrapper.in(Wallpaper::getResolution, resolution);

        if (condition == 1)
            wrapper.orderByDesc(Wallpaper::getCreateTime);
        if (condition == 2)
            wrapper.orderByDesc(Wallpaper::getCurrentSubscribers);
        if (condition == 3)
            wrapper.orderByDesc(Wallpaper::getRating);

        Page<Wallpaper> page = new Page<>(pageNum, pageSize);

        Page<Wallpaper> wallpaperPage = wallpaperMapper.selectPage(page, wrapper);
        log.info("total: {}", wallpaperPage.getTotal());
        List<Wallpaper> wallpaperList = wallpaperPage.getRecords();
        for (Wallpaper wallpaper : wallpaperList) {
            WallpaperVO wallpaperVO = new WallpaperVO(wallpaper);
            list.add(wallpaperVO);
        }
        wallpaperListVO.setList(list);
        wallpaperListVO.setTotal(wallpaperPage.getTotal());
        return wallpaperListVO;
    }
}
