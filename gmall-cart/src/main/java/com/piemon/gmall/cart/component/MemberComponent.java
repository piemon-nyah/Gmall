package com.piemon.gmall.cart.component;

import com.alibaba.fastjson.JSON;
import com.piemon.gmall.cart.vo.Cart;
import com.piemon.gmall.cart.vo.UserCartKey;
import com.piemon.gmall.constant.CartConstant;
import com.piemon.gmall.constant.SysCacheConstant;
import com.piemon.gmall.ums.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author: piemon
 * @date: 2020/11/3 22:49
 * @description:
 */
@Component
public class MemberComponent {
    @Autowired
    StringRedisTemplate redisTemplate;
    /**
     * 根据accessToken查询用户信息
     */
    public Member getMemberByAccessToken(String accessToken){
        String userJson = redisTemplate.opsForValue().get(SysCacheConstant.LOGIN_MEMBER + accessToken);
        Member member = JSON.parseObject(userJson, Member.class);
        return member;
    }

    /**
     *
     * @return
     */
    public UserCartKey getCartKey(String accessToken, String cartKey){
        UserCartKey userCartKey = new UserCartKey();
        Member member = null;
        if(!StringUtils.isEmpty(accessToken)){
           member = getMemberByAccessToken(accessToken);
        }

        if(member!=null){
            //获取到在线用户
            userCartKey.setLogin(true);
            userCartKey.setUserId(member.getId());
            userCartKey.setFinalCartKey(CartConstant.USER_CART_KEY_PREFIX+member.getId());
            return userCartKey;
        }else if(!StringUtils.isEmpty(cartKey)){
            //离线用户用离线购物车
            userCartKey.setLogin(false);
            userCartKey.setFinalCartKey(CartConstant.TEMP_CART_KEY_PREFIX+cartKey);
            return userCartKey;
        }else {
            //用户既没有登录也没有临时购物车
            String replace = UUID.randomUUID().toString().replace("-", "");
            userCartKey.setLogin(false);
            userCartKey.setFinalCartKey(CartConstant.TEMP_CART_KEY_PREFIX+replace);
            userCartKey.setTempCartKey(replace);
            return userCartKey;
        }
    }
}
