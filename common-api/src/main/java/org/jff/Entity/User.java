package org.jff.Entity;
import lombok.Data;


@Data
public class User{

    private Long userId;

    private String username;

    private String password;

    private String avatarUrl;

}
