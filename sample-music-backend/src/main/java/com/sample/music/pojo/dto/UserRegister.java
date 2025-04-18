package com.sample.music.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserRegister {
    private Long id;
    @NotEmpty
    @Email
    private String email;
    // 转化为json数据时忽略该项
    @JsonIgnore
    private String password;
    private String InvitationCode;
    private String VerificationCode;
}
