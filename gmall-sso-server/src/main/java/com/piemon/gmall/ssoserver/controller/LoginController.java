package com.piemon.gmall.ssoserver.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.piemon.gmall.constant.SysCacheConstant;
import com.piemon.gmall.to.CommonResult;
import com.piemon.gmall.ums.entity.Member;
import com.piemon.gmall.ums.service.MemberService;
import com.piemon.gmall.vo.ums.LoginResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: piemon
 * @date: 2020/11/1 0:39
 * @description:
 */
@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Reference
    MemberService memberService;

    @ResponseBody
    @GetMapping("/userinfo")
    public CommonResult getUserInfo(@RequestParam("accessToken") String accessToken){

        String redisKey = SysCacheConstant.LOGIN_MEMBER+accessToken;
        String member = redisTemplate.opsForValue().get(redisKey);
        //用户详细信息
        Member loginMember = JSON.parseObject(member, Member.class);
        loginMember.setId(null);
        loginMember.setPassword(null);
        return new CommonResult().success(loginMember);
    }

    @ResponseBody
    @PostMapping("/applogin")
    public CommonResult loginForGmall(@RequestParam("username") String username,
                                      @RequestParam("password") String password){
       Member member = memberService.login(username,password);

       if(member==null){
           //用户没有
           CommonResult result = new CommonResult().failed();
           result.setMessage("账号密码不匹配，请重新登录");
           return result;
       }else{
           String token = UUID.randomUUID().toString().replace("-", "");
           String memberJSON = JSON.toJSONString(member);
           redisTemplate.opsForValue().set(SysCacheConstant.LOGIN_MEMBER+token,memberJSON,SysCacheConstant.LOGIN_MEMBER_TIMEOUT, TimeUnit.MINUTES);

           LoginResponseVo vo = new LoginResponseVo();
           BeanUtils.copyProperties(member,vo);
           //设置访问令牌
           vo.setAccessToken(token);
           return new CommonResult().success(vo);
       }
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "redirec_url") String redirec_url,
                        @CookieValue(value = "sso_user",required = false)String ssoUser,
                        HttpServletResponse response,
                        Model model) throws IOException {

        if(!StringUtils.isEmpty(ssoUser)){
            //登录过，回到之前的地方，并且将当前ssoserver获取到的cookie以url的方式传递给其他域名
//            String url = redirec_url+"?"+"sso_user="+ssoUser;
//            response.sendRedirect(url);
            //return null;
            return redirec_url+"?"+"sso_user="+ssoUser;
        }else {
            //没登录过
            model.addAttribute("redirec_url",redirec_url);
            return "login";
        }
    }

    @PostMapping("/doLogin")
    public void doLogin(String username,String password,String redirec_url,
                          HttpServletResponse response) throws IOException {
        //1、模拟用户信息
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);

        //2、以上表示用户已经登录
        String token = UUID.randomUUID().toString().replace("-", "");

        redisTemplate.opsForValue().set(token,JSON.toJSONString(map));
        //3、登录成功，做两件事
        //3.1)、命令浏览器把当前的token保存为cookie sso_user=cookie
        Cookie cookie = new Cookie("sso_user",token);
        response.addCookie(cookie);
        //3.2)、命令浏览器重定向到他之前的位置

        response.sendRedirect(redirec_url+"?"+"sso_user="+token);
    }

}
