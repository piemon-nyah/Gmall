package com.piemon.gmall.oms.service.impl;

import com.piemon.gmall.oms.entity.OrderItem;
import com.piemon.gmall.oms.mapper.OrderItemMapper;
import com.piemon.gmall.oms.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
