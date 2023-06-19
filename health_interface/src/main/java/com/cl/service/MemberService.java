package com.cl.service;

import com.cl.pojo.Member;

import java.util.List;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/28
 * @description:
 * @version:1.0
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonths(List<String> months);
}
