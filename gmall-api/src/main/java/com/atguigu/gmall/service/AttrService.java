package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.PmsBaseAttrInfo;
import com.atguigu.gmall.entity.PmsBaseAttrValue;
import com.atguigu.gmall.entity.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {

    List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id);

    void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValue(String attrId);

    List<PmsBaseSaleAttr> getBaseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
