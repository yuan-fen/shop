package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuProductService {

    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getPmsSkuInfo(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrListBySpu(String productId);

    List<PmsSkuInfo> getAllSku();

    boolean checkPrice(String productSkuId, BigDecimal price);
}
