package com.atguigu.gmall.member.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.entity.UmsMember;
import com.atguigu.gmall.entity.UmsMemberReceiveAddress;
import com.atguigu.gmall.member.mapper.UmsMemberMapper;
import com.atguigu.gmall.member.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.service.UmsMemberService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllMembers() {

        List<UmsMember> memberList = umsMemberMapper.selectAllMembers();

        return memberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getAddressByMemberId(String memberId) {

        Example example = new Example(UmsMemberReceiveAddress.class);

        example.createCriteria().andEqualTo("memberId",memberId);

        List<UmsMemberReceiveAddress> addressList = umsMemberReceiveAddressMapper.selectByExample(example);

        return addressList;
    }

    @Override
    public UmsMember getMemberById(UmsMember umsMember) {

        return umsMemberMapper.selectByPrimaryKey(umsMember);

    }

    @Override
    public void saveMember(UmsMember umsMember) {

        umsMemberMapper.insert(umsMember);

    }

    @Override
    public int removeMember(UmsMember umsMember) {


        int result = umsMemberMapper.delete(umsMember);

        return result;
    }

    @Override
    public int updateMember(UmsMember umsMember) {

        int result = umsMemberMapper.updateByPrimaryKey(umsMember);

        return result;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null){
                // 原本需要将密码进行md5加密
                //用户以前可能登陆过，redis中存有用户信息，先从redis缓存中查找，
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + umsMember.getUsername() + ":info");

                if (StringUtils.isNotBlank(umsMemberStr)){
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }

            }
            // 用户之前没有登录过，umsMemberStr为空，链接redis失败，开启数据库
            UmsMember umsMemberFromDB = loginFromDB(umsMember);
            if (umsMemberFromDB != null){
                jedis.setex("user:" + umsMember.getPassword() + umsMember.getUsername() + ":info",60*60*24,JSON.toJSONString(umsMemberFromDB));
            }
            return umsMemberFromDB;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }

        return null;

    }

    @Override
    public void addUserToken(String token, String memberId) {

        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:" + memberId + ":token",60*60*2,token);
        jedis.close();

    }

    @Override
    public UmsMember checkOauthorUser(String sourceUid) {

        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("sourceUid",sourceUid);
        UmsMember umsMember = umsMemberMapper.selectOneByExample(example);

        return umsMember;
    }

    @Override
    public UmsMember addOauthorUser(UmsMember umsMember) {
         umsMemberMapper.insertSelective(umsMember);
         return umsMember;
    }

    @Override
    public UmsMemberReceiveAddress getAddressById(String receiveAddressId) {

        UmsMemberReceiveAddress umsMemberReceiveAddress = umsMemberReceiveAddressMapper.selectByPrimaryKey(receiveAddressId);

        return umsMemberReceiveAddress;
    }

    private UmsMember loginFromDB(UmsMember umsMember){

        List<UmsMember> umsMemberList = umsMemberMapper.select(umsMember);

        if (umsMemberList != null){
            UmsMember umsMemberFromDB = umsMemberList.get(0);
            return umsMemberFromDB;
        }
        return null;
    }
}
