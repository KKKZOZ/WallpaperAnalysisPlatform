package org.jff;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.SteamComment;
import org.jff.Entity.Wallpaper;
import org.jff.global.NotResponseBody;
import org.jff.vo.WallpaperDetails;
import org.jff.vo.WallpaperVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallpaper")
@AllArgsConstructor
public class WallpaperController {

    private final WallpaperService wallpaperService;


    @GetMapping()
    // 获取单个壁纸的所有信息
    public WallpaperDetails getWallpaperById(@RequestParam Long wallpaperId){
        return wallpaperService.getWallpaperById(wallpaperId);
    }

    @GetMapping("/comment")
    // 获取单个壁纸的所有评论
    public List<SteamComment> getCommentListByWallpaperId(@RequestParam Long wallpaperId){
        return wallpaperService.getCommentListByWallpaperId(wallpaperId);
    }

    @GetMapping("/voList")
    @NotResponseBody("")
    public List<WallpaperVO> getWallpaperVOListBySetId(@RequestParam Long setId){
        return wallpaperService.getWallpaperVOListBySetId(setId);
    }

}
