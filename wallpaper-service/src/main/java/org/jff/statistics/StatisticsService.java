package org.jff.statistics;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.Wallpaper;
import org.jff.WallpaperMapper;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatisticsService {

    private final WallpaperMapper wallpaperMapper;

    public List<TimelineCategoryCountItem>
    getTimelineCategoryCount(Integer year, Integer month) {
        Month month1 = Month.of(month);
        List<TimelineCategoryCountItem> res = new ArrayList<>();
        List<String> categoryList = Category.getCategoryList();
        for(String category : categoryList) {
            log.info(category);
            LambdaQueryWrapper<Wallpaper> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Wallpaper::getCategory,category);
            List<Wallpaper> wallpaperList = wallpaperMapper.selectList(queryWrapper);
            long count = wallpaperList.stream()
                    .filter(wallpaper ->
                            wallpaper.getCreateTime().getYear() == year&& wallpaper.getCreateTime().getMonth()==month1)
                    .count();
            TimelineCategoryCountItem timelineCategoryCountItem = new TimelineCategoryCountItem();
            timelineCategoryCountItem.setCategory(category);
            timelineCategoryCountItem.setCount((int)count);
            timelineCategoryCountItem.setLabel(String.valueOf(year)+String.valueOf(month));
            res.add(timelineCategoryCountItem);
        }
        return res;
    }
}
