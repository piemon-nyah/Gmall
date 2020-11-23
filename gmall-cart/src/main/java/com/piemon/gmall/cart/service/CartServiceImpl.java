package com.piemon.gmall.cart.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.piemon.gmall.cart.component.MemberComponent;
import com.piemon.gmall.cart.vo.Cart;
import com.piemon.gmall.cart.vo.CartItem;
import com.piemon.gmall.cart.vo.CartResponse;
import com.piemon.gmall.cart.vo.UserCartKey;
import com.piemon.gmall.constant.CartConstant;
import com.piemon.gmall.pms.entity.Product;
import com.piemon.gmall.pms.entity.SkuStock;
import com.piemon.gmall.pms.service.ProductService;
import com.piemon.gmall.pms.service.SkuStockService;
import com.piemon.gmall.ums.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: piemon
 * @date: 2020/11/3 21:38
 * @description:
 */
@Slf4j
@Component
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberComponent memberComponent;

    @Autowired
    RedissonClient redissonClient;

    @Reference
    SkuStockService skuStockService;

    @Reference
    ProductService productService;
    @Override
    public CartResponse addToCart(Long skuId, Integer num, String cartKey, String accessToken) throws ExecutionException, InterruptedException {
        //0、根据accessToken获取用户的id
        Member member = memberComponent.getMemberByAccessToken(accessToken);

        if(member!=null&&!StringUtils.isEmpty(cartKey)){
            //先去合并再说
            mergeCart(cartKey,member.getId());
        }
        //获取用户真正能使用的购物车
        UserCartKey userCartKey = memberComponent.getCartKey(accessToken, cartKey);

        String finalCartKey = userCartKey.getFinalCartKey();

        CartItem cartItem = addItemToCart(skuId,num,finalCartKey);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItem(cartItem);
        //设置临时购物车用户的cartKey，返回购物项
        cartResponse.setCartKey(userCartKey.getTempCartKey());
        //返回整个购物车
        CartResponse response = listCart(cartKey, accessToken);
        cartResponse.setCart(response.getCart());
        return cartResponse;
//        原方法 后抽取到MemberComponent中
//        if(member != null){
//            //1、这个人登录了，就用他的在线购物车 cart:user:1
//            /**
//             * 1、按照skuId找到sku的真正信息
//             * 2、给指定的购物车添加记录
//             *    如果已经有了这个skuId只是count的增加;
//             *
//             *skuId,cartKey
//             */
//            finalCartKey = CartConstant.USER_CART_KEY_PREFIX+member.getId();
//
//            return cartResponse;
//        }
//
//        if(!StringUtils.isEmpty(cartKey)){
//            //2、这个人没登录，用他的离线购物车 cart:temp:cartKey
//            finalCartKey = CartConstant.TEMP_CART_KEY_PREFIX+cartKey;
//            //添加到购物车
//            CartItem cartItem = addItemToCart(skuId,num,finalCartKey);
//            CartResponse cartResponse = new CartResponse();
//            cartResponse.setCartItem(cartItem);
//
//        }
//
//        //3、如果以上都没有，说明刚来，分配一个临时购物车
//        String newCartKey = UUID.randomUUID().toString().replace("-", "");
//        finalCartKey = CartConstant.TEMP_CART_KEY_PREFIX+newCartKey;
//        CartItem cartItem = addItemToCart(skuId,num,finalCartKey);
//        CartResponse cartResponse = new CartResponse();
//        cartResponse.setCartItem(cartItem);
//        cartResponse.setCartKey(newCartKey);
//        return cartResponse;
    }

    @Override
    public CartResponse updateCartItem(Long skuId, Integer num, String cartKey, String accessToken) {
        //1、判断购物车用哪个key
        UserCartKey userCartKey = memberComponent.getCartKey(accessToken, cartKey);

        String finalCartKey = userCartKey.getFinalCartKey();
        //获取之前购物车中购物项
        RMap<String, String> map = redissonClient.getMap(finalCartKey);
        String json = map.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(json, CartItem.class);
        //更新购物项数量
        cartItem.setCount(num);
        //更新后转为json存入redis
        String jsonString = JSON.toJSONString(cartItem);
        map.put(skuId.toString(),jsonString);

        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItem(cartItem);
        cartResponse.setCartKey(cartKey);

        return cartResponse;
    }

    @Override
    public CartResponse listCart(String cartKey, String accessToken) {
        UserCartKey userCartKey = memberComponent.getCartKey(accessToken, cartKey);
        //1、查询用户购物车的时候需要判断购物车是否需要合并
        if(userCartKey.isLogin()){
            //用户登录了，就需要合并购物车
            mergeCart(cartKey,userCartKey.getUserId());
        }

        //查询出购物车的数据
        String finalCartKey = userCartKey.getFinalCartKey();
        RMap<String, String> map = redissonClient.getMap(finalCartKey);
        //自动续期,设置过期时间
        //redisTemplate.expire(finalCartKey,30L, TimeUnit.DAYS);
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();

        CartResponse cartResponse = new CartResponse();

        if(map!=null&&!map.isEmpty()){
            map.entrySet().forEach((item)->{
                String value = item.getValue();
                CartItem cartItem = JSON.parseObject(value, CartItem.class);
                cartItems.add(cartItem);
            });
            cart.setCartItems(cartItems);
        }else {
            //用户没有购物车，就要新建一个购物车
            cartResponse.setCartKey(userCartKey.getTempCartKey());
        }
        cartResponse.setCart(cart);

        return cartResponse;
    }

    @Override
    public CartResponse deleteCart(Long skuId, String cartKey, String accessToken) {

        UserCartKey userCartKey = memberComponent.getCartKey(accessToken, cartKey);
        String finalCartKey = userCartKey.getFinalCartKey();
        //获取购物车，删除购物项
        RMap<String, String> map = redissonClient.getMap(finalCartKey);
        map.remove(skuId.toString());

        //删除完，重新查一遍购物车，返回整个购物车
        CartResponse cartResponse = listCart(cartKey, accessToken);
        return cartResponse;
    }

    @Override
    public CartResponse clearCart(String cartKey, String accessToken) {
        UserCartKey userCartKey = memberComponent.getCartKey(accessToken, cartKey);
        String finalCartKey = userCartKey.getFinalCartKey();
        RMap<Object, Object> map = redissonClient.getMap(finalCartKey);
        map.clear();
        CartResponse cartResponse = new CartResponse();
        return cartResponse;
    }

    @Override
    public CartResponse checkCartItems(String skuIds, Integer ops, String cartKey, String accessToken) {

        List<Long> skuIdsList = new ArrayList<>();

        UserCartKey userCartKey = memberComponent.getCartKey(accessToken, cartKey);
        String finalCartKey = userCartKey.getFinalCartKey();
        RMap<String, String> cart = redissonClient.getMap(finalCartKey);
        boolean checked = ops == 1?true:false;
        //修改一个或多个购物项状态
        if(!StringUtils.isEmpty(skuIds)){
            String[] ids = skuIds.split(",");
            for(String id:ids){
                long skuId = Long.parseLong(id);
                skuIdsList.add(skuId);
                //1、找到每个skuId对应的购物车中的json，把状态check改为ops对应的值
                //找到购物车中这个sku进行状态修改
                if(cart!=null&&!cart.isEmpty()){
                    String jsonValue = cart.get(id);
                    //将map(cart)中的json转换为CartItem
                    CartItem cartItem = JSON.parseObject(jsonValue, CartItem.class);
                    cartItem.setCheck(checked);
                    //覆盖redis中的数据
                    cart.put(id,JSON.toJSONString(cartItem));
                }
            }
        }
        //2、为了找到哪个被选中了，我们单独维护了数组，数组在map中用的key是checked,checked的值是一个Set集合
        //修改checked集合的状态
        String checkedJson = cart.get(CartConstant.CART_CHECKED_KEY);
        Set<Long> longSet = JSON.parseObject(checkedJson, new TypeReference<Set<Long>>(){});
        //防止空指针
        if(longSet==null||longSet.isEmpty()){
            longSet = new HashSet<>();
        }
        if(checked){
            //如果当前操作都是选中购物项
            longSet.addAll(skuIdsList);
            log.info("被选中的商品{}",longSet);
            //重新保存被选中的商品
        }else {
            longSet.removeAll(skuIdsList);
            log.info("被移除的商品{}",longSet);
        }
        cart.put(CartConstant.CART_CHECKED_KEY,JSON.toJSONString(longSet));
        //3、
        return null;
    }

    /**
     * 添加商品到指定购物车
     * @param skuId
     * @param finalCartKey
     * @param num 商品个数
     * @return
     */
    private CartItem addItemToCart(Long skuId, Integer num, String finalCartKey) throws ExecutionException, InterruptedException {

        CartItem newCartItem = new CartItem();
        /**
         * 1、只接受上一步的结果
         * thenAccept(r){
         *     r:上一步的结果
         * }
         * 2、thenApply(r){
         *     r:把上一步的结果进行修改再返回
         * }
         *
         */
        CompletableFuture<Void> skuFuture = CompletableFuture.supplyAsync(() -> {
            SkuStock skuStock = skuStockService.getById(skuId);
            return skuStock;
        }).thenAcceptAsync((stock)->{
            //拿到上一步的商品id
            Long productId = stock.getProductId();
            Product product = productService.getById(productId);
            //拿到上一步结果整体封装
            BeanUtils.copyProperties(stock,newCartItem);
            newCartItem.setSkuId(stock.getId());
            newCartItem.setName(product.getName());
            newCartItem.setCount(num);
            newCartItem.setCheck(true);
        });


        //0、查出skuId在数据库对应的最新详情,远程查询


        //购物车集合，k[skuId]是str，v[购物项]是str(json)
        RMap<String, String> map = redissonClient.getMap(finalCartKey);
        //获取购物车中这个skuId对应的购物项
        String itemJson = map.get(skuId.toString());
        skuFuture.get();//在线等异步结果
        //检查购物车中是否已经有这个购物项
        if(!StringUtils.isEmpty(itemJson)){
            //购物项是数量叠加,购物车老item只用来获取数量，给新的cartItem里面添加信息
            CartItem oldItem = JSON.parseObject(itemJson, CartItem.class);
            Integer count = oldItem.getCount();

            //等到异步任务完成,newCartItem才能用
            newCartItem.setCount(count+newCartItem.getCount());
            //新购物项数据覆盖老数据
            String string = JSON.toJSONString(newCartItem);
            map.put(skuId.toString(),string);
        }else {

            //新增购物项
            String string = JSON.toJSONString(newCartItem);
            map.put(skuId.toString(),string);
        }
        return newCartItem;
    }

    /**
     *
     * @param cartKey 老购物车
     * @param id 用户ID
     */
    private void mergeCart(String cartKey, Long id){
        String oldCartKey = CartConstant.TEMP_CART_KEY_PREFIX+cartKey;
        String userCartKey = CartConstant.USER_CART_KEY_PREFIX+id.toString();

        //获取到老购物车的数据
        RMap<String, String> map = redissonClient.getMap(oldCartKey);
        if(map!=null && !map.isEmpty()){
            //map不是null且里面有数据才需要合并
            map.entrySet().forEach((item)->{
                //skuId
                String key = item.getKey();
                //购物项的json数据
                String value = item.getValue();
                CartItem cartItem = JSON.parseObject(value, CartItem.class);
                //老购物车添到新购物车
                try {
                    addItemToCart(Long.parseLong(key),cartItem.getCount(),userCartKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        //合并完移除老购物车
        map.clear();
    }
}
