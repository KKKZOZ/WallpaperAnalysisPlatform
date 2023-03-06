package org.jff.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jff.global.NotResponseBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private Long userId;
    private String username;
    private String email;
    private String avatarUrl;
}
