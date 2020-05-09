package com.atguigu.gmall.member.mapper;

import com.atguigu.gmall.entity.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UmsMemberMapper extends Mapper<UmsMember> {


    List<UmsMember> selectAllMembers();
}
