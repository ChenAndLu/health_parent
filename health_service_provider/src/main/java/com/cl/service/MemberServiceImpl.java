package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.dao.MemberDao;
import com.cl.pojo.Member;
import com.cl.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/28
 * @description:
 * @version:1.0
 */

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> list = new ArrayList<>();
        if (months !=null && months.size() > 0){
            for (String month : months){
                month += ".31";
                Integer count = memberDao.findMemberCountBeforeDate(month);
                list.add(count);
            }
        }
        return list;
    }
}
