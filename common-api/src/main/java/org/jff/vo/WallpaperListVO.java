package org.jff.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WallpaperListVO {

    private List<WallpaperVO> list;

    private Long total;
}
