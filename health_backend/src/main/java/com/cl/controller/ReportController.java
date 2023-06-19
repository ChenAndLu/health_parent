package com.cl.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cl.constant.MessageConstant;
import com.cl.entity.Result;
import com.cl.service.MemberService;
import com.cl.service.ReportService;
import com.cl.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: ChenLu
 * @date: Created in 2023/4/2
 * @description:
 * @version:1.0
 */

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Map data = new HashMap();
        List<String> months = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        for (int i =0 ; i < 12;i++){
            calendar.add(Calendar.MONTH,1);
            months.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        data.put("months",months);
        List<Integer> memberCount = memberService.findMemberCountByMonths(months);
        data.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,data);
    }


    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        Map map = new HashMap();
        List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();

        List<String> setmealNames = new ArrayList<>();
        for (Map<String,Object> data : setmealCount){
            String name = (String)data.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",setmealCount);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> result = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 运营数据导出
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> result = null;
        //调用Service获取运营数据
        try {
           result = reportService.getBusinessReport();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //使用POI将查询到的数据写入到EXCEL文件
            String templateDirPath = request.getSession().getServletContext().getRealPath("template");
            String filePath = templateDirPath +File.separator+"report_template.xlsx";
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //获取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);

            sheet.getRow(2).getCell(5).setCellValue(reportDate);
            sheet.getRow(4).getCell(5).setCellValue(todayNewMember);
            sheet.getRow(4).getCell(7).setCellValue(totalMember);

            sheet.getRow(5).getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            sheet.getRow(5).getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            sheet.getRow(7).getCell(5).setCellValue(todayOrderNumber);//今日预约数
            sheet.getRow(7).getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            sheet.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            sheet.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            sheet.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            sheet.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                XSSFRow row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(out);

            out.flush();
            out.close();
            excel.close();

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }


        //下载文件
        return null;
    }
}
