package com.clt.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserChangeDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
}
