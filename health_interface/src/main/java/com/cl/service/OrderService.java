package com.cl.service;

import com.cl.entity.Result;

import java.util.Map;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/27
 * @description:
 * @version:1.0
 */
public interface OrderService {
    Result order(Map map);

    Map findById(Integer id) throws Exception;
}
