package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.PmsProductImage;
import com.atguigu.gmall.entity.PmsProductInfo;
import com.atguigu.gmall.entity.PmsProductSaleAttr;
import com.atguigu.gmall.entity.PmsSkuInfo;

import java.util.List;

public interface SpuProductService {

    List<PmsProductInfo> getSpuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId);

    List<PmsProductImage> getSpuImageList(String spuId);

    List<PmsProductSaleAttr> getProductSaleAttr(String productId,String skuId);

}
