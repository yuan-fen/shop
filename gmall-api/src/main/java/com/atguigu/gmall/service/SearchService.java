package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.PmsSearchParam;
import com.atguigu.gmall.entity.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
