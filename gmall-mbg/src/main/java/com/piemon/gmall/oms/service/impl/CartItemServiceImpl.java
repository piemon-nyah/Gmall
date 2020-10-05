package com.piemon.gmall.oms.service.impl;

import com.piemon.gmall.oms.entity.CartItem;
import com.piemon.gmall.oms.mapper.CartItemMapper;
import com.piemon.gmall.oms.service.CartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

}
