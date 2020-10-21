package com.piemon.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piemon.gmall.constant.EsConstant;
import com.piemon.gmall.pms.entity.*;
import com.piemon.gmall.pms.mapper.*;
import com.piemon.gmall.pms.service.ProductService;
import com.piemon.gmall.to.es.EsProduct;
import com.piemon.gmall.to.es.EsProductAttributeValue;
import com.piemon.gmall.to.es.EsSkuProductInfo;
import com.piemon.gmall.vo.PageInfoVo;
import com.piemon.gmall.vo.product.PmsProductParam;
import com.piemon.gmall.vo.product.PmsProductQueryParam;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author piemon
 * @since 2020-10-05
 */
@Slf4j
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    ProductFullReductionMapper productFullReductionMapper;

    @Autowired
    ProductLadderMapper productLadderMapper;

    @Autowired
    SkuStockMapper skuStockMapper;

    @Autowired
    JestClient jestClient;

    //当前线程共享同样的数据
    ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {

        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        if (param.getBrandId()!=null){
            //前端传了品牌
            wrapper.eq("brand_id",param.getBrandId());
        }

        if(!StringUtils.isEmpty(param.getKeyword())){
            //商品名字模糊查询
            wrapper.like("name",param.getKeyword());
        }

        if(param.getProductCategoryId()!=null){
            //商品分类查询
            wrapper.eq("product_category_id",param.getProductCategoryId());
        }

        if(!StringUtils.isEmpty(param.getProductSn())){
            //商品货号
            wrapper.like("product_sn",param.getProductSn());
        }

        if(param.getVerifyStatus()!=null){
            //发布状态
            wrapper.eq("publish_status",param.getPublishStatus());
        }

        if(param.getVerifyStatus()!=null){
            //商品是否审核通过
            wrapper.eq("verify_status",param.getVerifyStatus());
        }

        IPage<Product> page = productMapper.selectPage(new Page<Product>(param.getPageNum(), param.getPageSize()), wrapper);

        PageInfoVo pageInfoVo = new PageInfoVo(page.getTotal(),page.getPages(),
                param.getPageSize(),page.getRecords(),page.getCurrent());

        return pageInfoVo;
    }

    /**
     * 考虑事务
     * @param productParam
     *
     *  (1)哪些东西是一定要回滚的，哪些出错不必要回滚
     *     商品的核心信息（基本数据、sku）保存的时候，不要受到无关信息的影响。
     *     无关信息出问题，核心信息也不用回滚
     *  (2)事务的传播行为 propagation：当前方法的事务如何传播下去（里面的方法如果用事务是否和他共用一个事务）
     *      REQUIRED：(必须)：
     *          如果以前有事务，就和之前的事务共用一个事务，没有就创建一个事务
     *      SUPPORTS：(支持)
     *          之前有事务，就以事务的方式运行，没有事务也可以
     *      MANDATORY：(强制)
     *          一定要有事务，没有事务就报错
     *      REQUIRES_NEW：(总是用新的事务)
     *          创建一个新的事务，如果以前有事务，暂停前面的事务
     *      NOT_SUPPORTED：(不支持)
     *          不支持在事务内运行，如果已经有事务，就挂起当前存在的事务
     *      NEVER：(从不使用)
     *          不支持在事务内运行，如果已经有事务，抛异常
     *      NESTED：(嵌入式)
     *          开启一个子事务(mysql不支持)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveProduct(PmsProductParam productParam) {
        ProductServiceImpl currentProxy = (ProductServiceImpl) AopContext.currentProxy();
        //pms_product:保存商品基本信息
        currentProxy.saveBaseInfo(productParam);

        //pms_sku_stock:库存表
        currentProxy.saveSkuStock(productParam);

        //保存这个商品对应的所有属性的值
        currentProxy.saveProductAttributeValue(productParam);

        //pms_product_full_reduction:保存商品的满减信息
        currentProxy.saveFullReduction(productParam);

        //pms_product_ladder:阶梯价格表
        currentProxy.saveProductLadder(productParam);



    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {
        if(publishStatus == 0){
            //下架
            //改数据库状态和删es
            ids.forEach((id)->{
                setProductPublishStatus(publishStatus, id);
                deleteProductFromEs(id);
            });
        }
        else{
            //上架
            //改数据库状态和加es
            ids.forEach((id)->{
                //修改数据库状态
                setProductPublishStatus(publishStatus, id);
                //加es
                saveProductToEs(id);
            });
        }
    }

    private void deleteProductFromEs(Long id){
        Delete delete = new Delete.Builder(id.toString()).index(EsConstant.PRODUCT_ES_INDEX)
                .type(EsConstant.PRODUCT_INFO_ES_TYPE).build();
        try {
            DocumentResult execute = jestClient.execute(delete);
            if(execute.isSucceeded()){
                log.info("商品{}->下架成功",id);
            }else {
                //deleteProductFromEs(id);
                log.error("商品{}->es下架失败",id);
            }
        }catch (Exception e){
            //deleteProductFromEs(id);
            log.error("商品{}->es下架失败",id);
        }

    }

    private void saveProductToEs(Long id) {
        Product productInfo = productInfo(id);
        EsProduct esProduct = new EsProduct();
        //1、复制基本信息
        BeanUtils.copyProperties(productInfo,esProduct);

        //2、复制sku信息：对于es要保存商品信息,还要查出这个商品的sku,给es中保存
        List<SkuStock> stocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", id));
        List<EsSkuProductInfo> esSkuProductInfos = new ArrayList<>(stocks.size());

        //查出当前商品的sku属性
        List<ProductAttribute> skuAttributeNames = productAttributeValueMapper.selectProductSaleAttrName(id);

        stocks.forEach((skuStock) ->{
            EsSkuProductInfo esSkuProductInfo = new EsSkuProductInfo();
            BeanUtils.copyProperties(skuStock,esSkuProductInfo);


            String subTitle = esProduct.getName();
            if(StringUtils.isEmpty(skuStock.getSp1())){
                subTitle+=" "+skuStock.getSp1();
            }
            if(StringUtils.isEmpty(skuStock.getSp2())){
                subTitle+=" "+skuStock.getSp2();
            }
            if(StringUtils.isEmpty(skuStock.getSp3())){
                subTitle+=" "+skuStock.getSp3();
            }
            //sku的特色标题
            esSkuProductInfo.setSkuTitle(subTitle);

            List<EsProductAttributeValue> skuAttributeValues = new ArrayList<>();

            for(int i=0;i<skuAttributeNames.size();i++){
                EsProductAttributeValue value = new EsProductAttributeValue();
                value.setName(skuAttributeNames.get(i).getName());
                value.setProductId(id);
                value.setProductAttributeId(skuAttributeNames.get(i).getId());
                value.setType(skuAttributeNames.get(i).getType());

                //颜色 尺码让es去统计 改掉查询商品的属性分类里面所有属性的时候，按照sort字段排序好
                if(i==0){
                    value.setValue(skuStock.getSp1());
                }
                if(i==1){
                    value.setValue(skuStock.getSp2());
                }
                if(i==2){
                    value.setValue(skuStock.getSp3());
                }
                skuAttributeValues.add(value);
            }

            esSkuProductInfo.setAttributeValues(skuAttributeValues);
            //sku有多个销售属性：颜色、尺码

            esSkuProductInfos.add(esSkuProductInfo);
            //查出销售属性的名


        } );
        //查出这个sku所有销售属性对应的值。要统计数据库中这个sku有多少个值
        esProduct.setSkuProductInfos(esSkuProductInfos);
        //3、复制公共属性信息，查出这个商品的公共属性

        List<EsProductAttributeValue> attributeValues = productAttributeValueMapper.selectProductBaseAttrAndValue(id);
        esProduct.setAttrValueList(attributeValues);

        //把商品保存到es中
        try {
            Index build = new Index.Builder(esProduct)
                    .index(EsConstant.PRODUCT_ES_INDEX)
                    .type(EsConstant.PRODUCT_INFO_ES_TYPE)
                    .id(id.toString())
                    .build();
            DocumentResult execute = jestClient.execute(build);
            boolean succeeded = execute.isSucceeded();
            if(succeeded){
                log.info("ES中id为{}商品上架完成",id);
            }else{
                log.error("ES中id为{}商品未保存成功，开始重试",id);
                //saveProductToEs(id);
            }
        } catch (IOException e) {
            log.error("ES中id为{}的商品数据保存异常:{}",id,e.getMessage());
            //saveProductToEs(id);
        }
    }

    public void setProductPublishStatus(Integer publishStatus, Long id) {
        //javaBean应该都去用包装类型
        Product product = new Product();
        //product默认所有属性为null
        product.setId(id);
        product.setPublishStatus(publishStatus);

        //mybatisplus自带的更新方法是哪个字段有值就更哪个字段
        productMapper.updateById(product);
    }

    @Override
    public Product productInfo(Long id) {
        return productMapper.selectById(id);
    }

    /**
     * 保存商品基础信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBaseInfo(PmsProductParam productParam){
        //pms_product:保存商品基本信息
        Product product = new Product();
        BeanUtils.copyProperties(productParam,product);//属性拷贝
        productMapper.insert(product);
        threadLocal.set(product.getId());
    }
    /**
     * 保存这个商品对应的所有属性的值
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductAttributeValue(PmsProductParam productParam){
        //pms_product_attribute_value:保存这个商品对应的所有属性的值
        List<ProductAttributeValue> valueList = productParam.getProductAttributeValueList();
        valueList.forEach((item)->{
            item.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(item);
        });
    }

    /**
     * 保存商品的满减信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFullReduction(PmsProductParam productParam) {
        List<ProductFullReduction> fullReductionList = productParam.getProductFullReductionList();
        fullReductionList.forEach((reduction)->{
            reduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(reduction);
        });
    }

    /**
     * 保存商品满减打折
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductLadder(PmsProductParam productParam) {
        List<ProductLadder> ladderList = productParam.getProductLadderList();
        ladderList.forEach((productLadder)->{
            productLadder.setProductId(threadLocal.get());
            productLadderMapper.insert(productLadder);
        });
    }

    /**
     * 保存商品库存
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSkuStock(PmsProductParam productParam) {
        List<SkuStock> skuStockList = productParam.getSkuStockList();

        for(int skuId=1;skuId<=skuStockList.size();skuId++){
            SkuStock skuStock = skuStockList.get(skuId-1);
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                //生成必备的skuCode 生成规则：商品id_sku自增id(1_1,1_2,1_3)
                skuStock.setSkuCode(threadLocal.get()+"_"+skuId);
            }
            skuStock.setId(threadLocal.get());
            skuStockMapper.insert(skuStock);
        }

    }
}
