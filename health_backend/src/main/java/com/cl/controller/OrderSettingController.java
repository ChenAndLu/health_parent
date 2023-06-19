package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cl.constant.MessageConstant;
import com.cl.entity.Result;
import com.cl.pojo.OrderSetting;
import com.cl.service.OrderSettingService;
import com.cl.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/25
 * @description:
 * @version:1.0
 */

@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            List<String[]> data = POIUtils.readExcel(excelFile);
            if (data != null && data.size() > 0){
                List<OrderSetting> list = new ArrayList<>();
                for (String[] rowData:data){
                    String date = rowData[0];
                    String number = rowData[1];
                    OrderSetting orderSetting = new OrderSetting(new SimpleDateFormat("yyyy/MM/dd").parse(date), Integer.parseInt(number));
                    list.add(orderSetting);
                }
                orderSettingService.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);

        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //获取预约设置数据成功
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            //获取预约设置数据失败
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);
            //预约设置成功
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            //预约设置失败
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
