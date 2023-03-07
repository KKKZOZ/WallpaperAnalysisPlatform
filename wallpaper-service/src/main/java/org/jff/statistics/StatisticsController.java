package org.jff.statistics;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final StatisticsService StatisticsService;

    @GetMapping("/timeline-category-count")
    public List<TimelineCategoryCountItem>
    getTimelineCategoryCount(@RequestParam Integer year,@RequestParam Integer month) {
        return StatisticsService.getTimelineCategoryCount(year,month);
    }
}
