package org.jff.vo;

import lombok.Data;

@Data
public class WallpaperVO {
    private Long wallpaperId;
    private String name;
    private String url;
    private int currentSubscribers;
}
