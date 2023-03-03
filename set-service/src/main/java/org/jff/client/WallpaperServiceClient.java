package org.jff.client;

import org.jff.vo.WallpaperVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("wallpaper-service") // nacos 服务 id
public interface WallpaperServiceClient {

    @GetMapping("/api/v1/wallpaper/voList")
    public List<WallpaperVO> getWallpaperVOListBySetId(@RequestParam Long setId);
}
