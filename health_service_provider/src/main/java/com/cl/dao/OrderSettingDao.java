package com.cl.dao;

import com.cl.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/25
 * @description:
 * @version:1.0
 */
public interface OrderSettingDao {
    Long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date parseString2Date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
