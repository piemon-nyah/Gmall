package com.piemon.gmall.vo.ums;

import lombok.Data;

/**
 * @author: piemon
 * @date: 2020/11/2 20:21
 * @description: 登录响应的vo
 */
@Data
public class LoginResponseVo {

    private Long memberLevelId;

    private String username;

    private String nickname;

    private String phone;

    private String accessToken;//访问令牌
}
