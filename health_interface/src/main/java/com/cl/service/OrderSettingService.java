package com.cl.service;

import com.cl.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/25
 * @description:
 * @version:1.0
 */
public interface OrderSettingService {
    void add(List<OrderSetting> list);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
