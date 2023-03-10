package org.jff;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.Entity.CommentType;
import org.jff.Entity.LikeStatus;
import org.jff.Entity.Set;
import org.jff.Entity.SetRecord;
import org.jff.client.CommentServiceClient;
import org.jff.client.UserServiceClient;
import org.jff.client.WallpaperServiceClient;
import org.jff.dto.SetDTO;
import org.jff.dto.SetLikeStatus;
import org.jff.global.ResponseVO;
import org.jff.global.ResultCode;
import org.jff.mapper.LikeStatusMapper;
import org.jff.mapper.SetRecordMapper;
import org.jff.util.StringUtil;
import org.jff.vo.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class SetService {

    private final SetMapper setMapper;
    private final LikeStatusMapper likeStatusMapper;

    private final SetRecordMapper setRecordMapper;

    private final WallpaperServiceClient wallpaperServiceClient;
    private final CommentServiceClient commentServiceClient;
    private final UserServiceClient userServiceClient;


    public List<Set> getSetListByUserId(Long userId,boolean onlyPublic){
        LambdaQueryWrapper<Set> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Set::getUserId, userId);
        wrapper.orderByAsc(Set::getCreateTime);
        if (onlyPublic) {
            wrapper.eq(Set::isPublic, true);
        }
        return setMapper.selectList(wrapper);
    }

    public ResponseVO createSet(Long userId, SetDTO setDTO) {
        log.info("userId: {}  setDTO: {}", userId, setDTO);
        Set set = new Set();
        set.setUserId(userId);
        set.setPublic(setDTO.isPublic());
        set.setSetName(setDTO.getSetName());
        set.setCoverUrl(setDTO.getCoverUrl());
        set.setCreateTime(LocalDateTime.now());
        setMapper.insert(set);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO updateSet(Long userId, SetDTO setDTO) {
        Optional<Set> optionalSet = setMapper.getSetById(setDTO.getSetId());
        if(optionalSet.isEmpty()){
            return new ResponseVO(ResultCode.SET_IS_NOT_FOUND);
        }
        Set set = optionalSet.get();
        if(set.getUserId() != userId){
            return new ResponseVO(ResultCode.AUTHORIZATION_ERROR);
        }
        set.setPublic(setDTO.isPublic());
        set.setSetName(setDTO.getSetName());
        if(StringUtil.isNotBlank(setDTO.getCoverUrl()))
            set.setCoverUrl(setDTO.getCoverUrl());
        setMapper.updateById(set);

        return new ResponseVO(ResultCode.SUCCESS);
    }

    public LikeStatus getLikeStatus(Long userId, Long setId) {
        return null;
    }

    public SetVO showSetById(Long userId, Long setId) {
        // ???????????????????????????set????????????????????????set??????????????????set??????wallpaper_list??????
        Optional<Set> optionalSet = setMapper.getSetById(setId);
        Set set = optionalSet.orElseThrow();
        SetVO setVO = new SetVO(set);
        int status = likeStatusMapper
                .getLikeStatusByUserIdAndObjectID(userId,setId,LikeStatusMapper.SET).getStatus();

        List<WallpaperVO> wallpaperList = wallpaperServiceClient
                .getWallpaperVOListBySetId(setId);
        List<CommentVO> commentList = commentServiceClient
                .getCommentListByObjectId(userId, setId, CommentType.SET);
        UserVO userVO = userServiceClient
                .getUserInfoList(List.of(set.getUserId())).get(0);
        PublisherVO publisherVO = new PublisherVO(userVO);
        setVO.setLikeStatus(status);
        setVO.setWallpaperList(wallpaperList);
        setVO.setCommentList(commentList);
        setVO.setPublisherInfo(publisherVO);
        setVO.setUpdatable(set.getUserId() == userId);
        return setVO;
    }

    public ResponseVO changeLikeStatus(Long userId, SetLikeStatus likeStatus) {
        // 1. ??????????????????????????????
//        int oldStatus = likeStatusMapper
//                .getStatusByUserIdAndSetId(userId,likeStatus.getSetId());
        LikeStatus newStatus = likeStatusMapper
                .getLikeStatusByUserIdAndObjectID(userId,likeStatus.getSetId(),LikeStatusMapper.SET);
        int oldStatus = newStatus.getStatus();
        int curStatus = likeStatus.getStatus();
        newStatus.setStatus(likeStatus.getStatus());
        likeStatusMapper.updateById(newStatus);

        Set set = setMapper.selectById(likeStatus.getSetId());
        set.updateLikeStatus(oldStatus,curStatus);
        setMapper.updateById(set);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO deleteSet(Long userId, Long setId) {
        Optional<Set> optionalSet = setMapper.getSetById(setId);
        if(optionalSet.isEmpty()){
            return new ResponseVO(ResultCode.SET_IS_NOT_FOUND);
        }
        Set set = optionalSet.get();
        if(set.getUserId() != userId){
            return new ResponseVO(ResultCode.AUTHORIZATION_ERROR);
        }
        setMapper.deleteById(setId);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO addWallpaperToSet(Long setId, Long wallpaperId) {
        SetRecord record = new SetRecord();
        record.setWallpaperId(wallpaperId);
        record.setSetId(setId);
        setRecordMapper.insert(record);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public ResponseVO deleteWallpaperFromSet(Long setId, Long wallpaperId) {
        LambdaQueryWrapper<SetRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetRecord::getSetId,setId);
        wrapper.eq(SetRecord::getWallpaperId,wallpaperId);
        setRecordMapper.delete(wrapper);
        return new ResponseVO(ResultCode.SUCCESS);
    }

    public List<SetInfoVO> recommendSet() {
        // TODO:????????????

        // ????????????????????????set
        // lambda
//        LambdaQueryWrapper<Set> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Set::isPublic, true);
        QueryWrapper<Set> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_public",true);
        List<Set> setList =
                new ArrayList<>(setMapper.selectList(queryWrapper).stream()
                        .filter(set -> (set.getCoverUrl() != null && !set.getCoverUrl().equals(""))).toList());

        Collections.shuffle(setList);
        List<SetInfoVO> list = new ArrayList<>();

        for(int i=0;i<=Math.min(7,setList.size()-1);i++){
            Set set = setList.get(i);
            SetInfoVO setInfoVO = new SetInfoVO();
            setInfoVO.setSetId(set.getSetId());
            setInfoVO.setSetName(set.getSetName());
            setInfoVO.setCoverUrl(set.getCoverUrl());
            list.add(setInfoVO);
        }
        return list;
    }

    public List<SetInfoVO> getSetInfoList(Long userId) {
        List<Set> setList = getSetListByUserId(userId,false);
        List<SetInfoVO> list = setList.stream()
                .map(SetInfoVO::new).toList();
        return list;
    }

    public Long getPublisherIdBySetId(Long setId) {
        return setMapper.selectById(setId).getUserId();
    }

    public Set getSetInfoBySetId(Long setId) {
        return setMapper.selectById(setId);
    }
}
