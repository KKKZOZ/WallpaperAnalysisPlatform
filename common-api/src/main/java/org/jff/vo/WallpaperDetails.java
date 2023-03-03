package org.jff.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jff.Entity.SteamComment;
import org.jff.Entity.Wallpaper;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WallpaperDetails extends Wallpaper {
    private List<SteamComment> commentList;

    public WallpaperDetails(Wallpaper wallpaper) {
        super(wallpaper);
    }

}
