package com.piemon.gmall.to.es;

import com.piemon.gmall.pms.entity.SkuStock;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: piemon
 * @date: 2020/10/19 20:33
 * @description:
 */
@Data
public class EsSkuProductInfo extends SkuStock implements Serializable {

    private String skuTitle;//sku的特定标题

    /**
     * 每个sku不同的属性以及他的值
     */
    List<EsProductAttributeValue> attributeValues;
}
