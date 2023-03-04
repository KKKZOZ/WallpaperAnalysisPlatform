package org.jff.client;

import org.jff.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/api/v1/user/list")
    public List<UserVO> getUserInfoList(@RequestParam List<Long> userIdList);
}
