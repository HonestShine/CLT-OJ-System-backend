package com.clt.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;  //编号
    private String username;    //用户名
    private String password;    //密码
    private Integer role;       //角色：1-普通用户，0-管理员
    private LocalDateTime createdAt;    //创建时间
    private String nickname;    //昵称
    private String hobby;   //爱好
    private String introduction;    //简介
    private String avatar;  //头像
}
