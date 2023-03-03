package org.jff.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jff.dto.UserDTO;
import org.jff.global.NotResponseBody;
import org.jff.global.ResponseVO;
import org.jff.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/list")
    @NotResponseBody("")
    // 根据用户id list，获取用户信息
    public List<UserVO> getUserInfoList(@RequestParam("userIdList") List<Long> userIdList){
        log.info("获取UserVO List");
        return userService.getUserInfoList(userIdList);
    }

    @PostMapping("/login")
    public ResponseVO login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/logout")
    public ResponseVO logout(){
        return userService.logout();
    }

    @PostMapping("/register")
    public ResponseVO register(@RequestBody User user){
        return userService.register(user);
    }

    @PutMapping()
    // 修改用户信息
    public ResponseVO updateUserInfo(@RequestHeader("userId") Long userId,
                                     @RequestBody User user){
        user.setUserId(userId);
        return userService.updateUserInfo(user);
    }


    @GetMapping("/dev")
    public String dev(){
        return "dev";
    }
}
