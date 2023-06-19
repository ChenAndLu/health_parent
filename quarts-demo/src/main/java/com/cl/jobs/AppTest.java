package com.cl.jobs;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/24
 * @description:
 * @version:1.0
 */

public class AppTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-jobs.xml");
    }
}
