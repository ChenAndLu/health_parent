package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.dao.MemberDao;
import com.cl.dao.OrderDao;
import com.cl.dao.SetmealDao;
import com.cl.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/4/3
 * @description:
 * @version:1.0
 */

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SetmealDao setmealDao;
    @Override
    public Map<String, Object> getBusinessReport() throws Exception {
        Map<String,Object> result = new HashMap<>();
        String today = DateUtils.parseDate2String(new Date());
        //获取当前时间
        result.put("reportDate", today);
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        Integer totalMember = memberDao.findMemberTotalCount();

        //动态获取本周一的日期
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);

        //本月新增会员数
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(monday);
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);

        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        List<Map<String,Object>> hotSetmeal = setmealDao.findHotSetmeal();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);

        return result;
    }
}
