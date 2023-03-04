package org.jff.vo;

import lombok.Data;
import org.jff.Entity.Wallpaper;

@Data
public class WallpaperVO {
    private Long wallpaperId;
    private String name;
    private String url;
    private int currentSubscribers;

    public WallpaperVO(Wallpaper wallpaper){
        this.wallpaperId = wallpaper.getWallpaperId();
        this.name = wallpaper.getName();
        this.url = wallpaper.getUrl();
        this.currentSubscribers = wallpaper.getCurrentSubscribers();
    }
}
