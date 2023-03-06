package org.jff;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.Set;
import org.jff.dto.SetDTO;
import org.jff.dto.SetLikeStatus;
import org.jff.global.NotResponseBody;
import org.jff.global.ResponseVO;
import org.jff.vo.SetInfoVO;
import org.jff.vo.SetVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/set")
@AllArgsConstructor
public class SetController {

    private final SetService setService;

    @GetMapping("/list")
    // 获取某个用户的集合列表
    // id,name,avatarUrl,isPublic
    public List<SetInfoVO> getSetInfoList(@RequestHeader("userId") Long myUserId,
                                          @RequestParam Long userId) {
        if (userId == -1)
            return setService.getSetInfoList(myUserId);
        else
            return setService.getSetInfoList(userId);
    }

//    public List<Set> getSetListByUserId(@RequestHeader("userId") Long myUserId,
//                                        @RequestParam Long userId,
//                                        @RequestParam boolean onlyPublic){
//        if(userId==null){
//            return setService.getSetListByUserId(myUserId,onlyPublic);
//        }else {
//            return setService.getSetListByUserId(userId,onlyPublic);
//        }
//    }

    @GetMapping
    // 某用户去获取其他用户的集合
    public SetVO showSetById(@RequestHeader("userId") Long userId,
                             @RequestParam Long setId) {
        log.info("userId: {}  setId: {}", userId, setId);
        return setService.showSetById(userId, setId);
    }

    @PostMapping()
    // 用户新建集合
    public ResponseVO createSet(@RequestHeader("userId") Long userId,
                                @RequestBody SetDTO setDTO) {
        return setService.createSet(userId, setDTO);
    }

    @PutMapping()
    // 用户修改自己的集合
    public ResponseVO updateSet(@RequestHeader("userId") Long userId,
                                @RequestBody SetDTO setDTO) {
        return setService.updateSet(userId, setDTO);
    }

    @DeleteMapping()
    // 用户删除自己的集合
    public ResponseVO deleteSet(@RequestHeader("userId") Long userId,
                                @RequestParam Long setId) {
        return setService.deleteSet(userId, setId);
    }

    @PostMapping("/wallpaper")
    // 用户往集合里添加壁纸
    public ResponseVO addWallpaperToSet(@RequestBody Map<String, Long> map) {
        return setService.addWallpaperToSet(map.get("setId"), map.get("wallpaperId"));
    }

    @DeleteMapping("/wallpaper")
    // 用户从集合里删除壁纸
    public ResponseVO deleteWallpaperFromSet(@RequestParam Long setId,
                                             @RequestParam Long wallpaperId) {
        return setService.deleteWallpaperFromSet(setId,wallpaperId);
    }


    @GetMapping("/recommend")
    // 随机推荐一些公开的集合，最多8个
    public List<SetInfoVO> recommendSet() {
        return setService.recommendSet();
    }


    @PostMapping("/likeStatus")
    // 用户进行点赞或者取消点赞
    public ResponseVO changeLikeStatus(@RequestHeader("userId") Long userId,
                                       @RequestBody SetLikeStatus likeStatus) {
        return setService.changeLikeStatus(userId, likeStatus);
    }

    @GetMapping("/publisherId")
    public Long getPublisherIdBySetId(@RequestParam("setId") Long setId) {
        return setService.getPublisherIdBySetId(setId);
    }

    @GetMapping("/info")
    @NotResponseBody("")
    // 根据setId获取集合的信息
    public Set getSetInfoBySetId(@RequestParam("setId") Long setId) {
        return setService.getSetInfoBySetId(setId);
    }


}
