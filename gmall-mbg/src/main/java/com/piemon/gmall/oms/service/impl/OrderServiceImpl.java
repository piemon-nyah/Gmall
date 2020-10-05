package com.piemon.gmall.oms.service.impl;

import com.piemon.gmall.oms.entity.Order;
import com.piemon.gmall.oms.mapper.OrderMapper;
import com.piemon.gmall.oms.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
