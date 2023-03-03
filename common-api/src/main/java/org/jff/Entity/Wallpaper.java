package org.jff.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("wallpaper_info")
@AllArgsConstructor
@NoArgsConstructor
public class Wallpaper {
    @TableId(type = IdType.AUTO)
    private Long wallpaperId;
    private String name;
    private String url;
    private String type;
    private String category;
    private int rating;
    private String resolution;
    private int uniqueVisitors;
    private int currentSubscribers;
    private int currentFavorites;
    private LocalDateTime createTime;

    public Wallpaper(Wallpaper wallpaper) {
        this.wallpaperId = wallpaper.wallpaperId;
        this.name = wallpaper.name;
        this.url = wallpaper.url;
        this.type = wallpaper.type;
        this.category = wallpaper.category;
        this.rating = wallpaper.rating;
        this.resolution = wallpaper.resolution;
        this.uniqueVisitors = wallpaper.uniqueVisitors;
        this.currentSubscribers = wallpaper.currentSubscribers;
        this.currentFavorites = wallpaper.currentFavorites;
        this.createTime = wallpaper.createTime;
    }
}
