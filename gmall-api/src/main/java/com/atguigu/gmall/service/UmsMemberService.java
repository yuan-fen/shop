package com.atguigu.gmall.service;

import com.atguigu.gmall.entity.UmsMember;
import com.atguigu.gmall.entity.UmsMemberReceiveAddress;

import java.util.List;

public interface UmsMemberService {

    List<UmsMember> getAllMembers();

    List<UmsMemberReceiveAddress> getAddressByMemberId(String memberId);

    UmsMember getMemberById(UmsMember umsMember);

    void saveMember(UmsMember umsMember);

    int removeMember(UmsMember umsMember);

    int updateMember(UmsMember umsMember);

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, String memberId);

    UmsMember checkOauthorUser(String sourceUid);

    UmsMember addOauthorUser(UmsMember umsMember);

    UmsMemberReceiveAddress getAddressById(String receiveAddressId);
}
