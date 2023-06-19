package com.cl.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.cl.dao.OrderSettingDao;
import com.cl.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/25
 * @description:
 * @version:1.0
 */

@Transactional
@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 添加预约信息
     * @param list 需要设置的日期以及可预约的数量
     */
    @Override
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() >0){
            for (OrderSetting orderSetting : list){
//                根据日期查询已经进行了预约设置
                SetNumberOfOrderSetting(orderSetting);
            }
        }
    }

    /**
     * 查询预约数据
     * @param orderDate 查询当前月
     * @return 返回t_orderSetting中的数据
     */
    @Override
    public List<Map> getOrderSettingByMonth(String orderDate) {
        String dateBegin = orderDate+"-1";
        String dateEnd = orderDate+"-31";
        Map<String,String> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        if (list != null && list.size() > 0){
            for (OrderSetting orderSetting : list){
                Date orderDate2 = orderSetting.getOrderDate();
                int number = orderSetting.getNumber();
                int reservations = orderSetting.getReservations();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderDate2);
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                HashMap<Object, Object> map2 = new HashMap<>();
                map2.put("date",date);
                map2.put("number",number);
                map2.put("reservations",reservations);
                data.add(map2);
            }
        }
        return data;
    }

    /**
     * 更新当前日期的预约数量
     * @param orderSetting 编辑的当前日期以及预约数量
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        SetNumberOfOrderSetting(orderSetting);
    }

    //设置日期中的预约数量
    private void SetNumberOfOrderSetting(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
}
