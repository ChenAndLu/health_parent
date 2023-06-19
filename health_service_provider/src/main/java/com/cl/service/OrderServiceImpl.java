package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.constant.MessageConstant;
import com.cl.dao.MemberDao;
import com.cl.dao.OrderDao;
import com.cl.dao.OrderSettingDao;
import com.cl.entity.Result;
import com.cl.pojo.Member;
import com.cl.pojo.Order;
import com.cl.pojo.OrderSetting;
import com.cl.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/27
 * @description:
 * @version:1.0
 */

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;
    /**
     * 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
     * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
     * 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
     * 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
     * 5、预约成功，更新当日的已预约人数，同时保存预约信息到预约表
     * @param map
     * @return
     */
    @Override
    public Result order(Map map) {

        try {
            Date orderDate = DateUtils.parseString2Date((String)map.get("orderDate"));
            OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
            if (orderSetting == null){
                //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            if (orderSetting.getReservations() >= orderSetting.getNumber()){
                //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
                return new Result(false, MessageConstant.ORDER_FULL);
            }
            String telephone = (String)map.get("telephone");
            Member member = memberDao.findByTelephone(telephone);
            if (member == null){
                //不是会员自动注册
                member = new Member();
                member.setName((String) map.get("name"));
                member.setPhoneNumber(telephone);
                member.setIdCard((String) map.get("idCard"));
                member.setSex((String) map.get("sex"));
                member.setRegTime(new Date());
                memberDao.add(member);
            }else {
                //检查用户是否重复预约
                Integer memberId = member.getId();
                Integer setmealId = (Integer)map.get("setmealId");
                Order order = new Order();
                order.setMemberId(memberId);
                order.setSetmealId(setmealId);
                order.setOrderDate(orderDate);
                List<Order> list = orderDao.findByCondition(order);
                if (list!=null && list.size() >0){
                    //重复预约
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }
            //更新以预约人数
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingDao.editReservationsByOrderDate(orderSetting);
            //保存预约信息
            Order order = new Order(member.getId(),
                    orderDate,
                    (String)map.get("orderType"),
                    Order.ORDERSTATUS_NO,
                    Integer.parseInt((String) map.get("setmealId")));
            orderDao.add(order);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
