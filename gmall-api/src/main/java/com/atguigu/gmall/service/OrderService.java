package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.OmsOrder;

public interface OrderService {
    String checkTradeCode(String memberId,String tradeCode);

    String createTradeCode(String memberId);

    OmsOrder saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderByOutTradeNo(String outTradeNo);

    void updateOrder(OmsOrder omsOrder);
}
