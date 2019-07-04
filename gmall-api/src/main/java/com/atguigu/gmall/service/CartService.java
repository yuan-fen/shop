package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.OmsCartItem;
import com.atguigu.gmall.entity.OmsOrderItem;

import java.util.List;

public interface CartService {
    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDB);

    void flushCartCache(String memberId);

    List<OmsCartItem> getCartList(String memberId);

    void checkCart(OmsCartItem omsCartItem);

    void removeCart(OmsOrderItem omsOrderItem);
}
