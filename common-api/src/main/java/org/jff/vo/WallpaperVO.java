package org.jff.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jff.Entity.Wallpaper;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
