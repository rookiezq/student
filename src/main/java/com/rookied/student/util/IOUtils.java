package com.rookied.student.util;

import com.rookied.student.bean.Course;
import com.rookied.student.bean.Score;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @desciption: IO工具类
 * @author: Demon
 * @version: 1.0 2019-03-28 20:25
 **/
@SuppressWarnings("all")
public class IOUtils {
    /**
     * 读取学生成绩xlsx文件
     */
    public static void readBookXlsx(String filePath,Map<String, Object> map) throws IOException {
        XSSFWorkbook xwb = new XSSFWorkbook(filePath);
        //因为工作表只有一个所以取0
        XSSFSheet sheet = xwb.getSheetAt(0);
        XSSFRow row;
        String cell;
        //学号
        String sid;
        List<Course> courseList = (List<Course>) map.get("courseList");
        List<Score> scoreList = (List<Score>) map.get("scoreList");
        //创建一个存放当前文件中课程的id
        List<Integer> cidList = new ArrayList<>();
        //获取学期
        int term = (int) map.get("term");
        //创建一个存放课程以及对应成绩
        //从表格第二行开始
        for (int i = sheet.getFirstRowNum() + 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            //获得单元表的当前行对象
            row = sheet.getRow(i);
            //遍历所有列
            for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                cell = row.getCell(j).toString();
                sid = row.getCell(1).toString();
                //课程名是在第三行第三个单元格开始
                if (i == sheet.getFirstRowNum() + 2 && j > row.getFirstCellNum() + 1) {
                    //判断课程名是否为空,进入下一次循环
                    if ("".equals(cell.trim())) {
                        continue;
                    }
                    //先从map中获得课程id再更新
                    int cid = (int) map.get("cid");
                    cid++;
                    cidList.add(cid);
                    map.put("cid",cid);
                    String major = (String) map.get("major");

                    //封装Course
                    Course course = new Course(cid, cell, term, major);
                    courseList.add(course);
                }
                //判断学号是否为空,进入下一次循环
                if ("".equals(sid.trim())) {
                    continue;
                }
                //成绩是从第四行第三个单元格开始
                if (i > sheet.getFirstRowNum() + 2 && j > row.getFirstCellNum() + 1) {
                    int cid = cidList.get(j-2);
                    //封装Score
                    Score score = new Score(cid,sid,cell);
                    scoreList.add(score);
                }
            }
        }
    }

    /**
     * 读取毕业信息xlsx文件
     *
     * @throws IOException
     */
    @Test
    public void readXlsx() throws IOException {
        XSSFWorkbook xwb = new XSSFWorkbook("C:\\Users\\Administrator\\Desktop\\毕设\\2014级 计科 会计 成绩\\就业  2014计科1班.xlsx");
        XSSFSheet sheet = xwb.getSheetAt(0);
        XSSFRow row;
        String cell;
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                cell = row.getCell(j).toString();
                System.out.print(cell + "\t");
            }
            System.out.println("");
        }
    }


    /**
     * 遍历目录下所有的.xlsx
     */
    public static Map<String, Object> findAllXlsx() throws IOException {
        Map<String, Object> map = new HashMap<>(16);
        List<Course> courseList = new LinkedList<>();
        List<Score> scoreList = new LinkedList<>();
        File file = new File("C:\\Users\\Administrator\\Desktop\\毕设\\2014级 计科 会计 成绩");
        if (!file.exists() || file.isFile()) {
            System.out.println("文件夹不存在");
        }

        File[] files = file.listFiles();
        if (files != null) {
            String filePath;
            //文件索引
            int index = 0;
            //存入课程id，学期，专业，课程List，成绩List
            map.put("courseList",courseList);
            map.put("scoreList",scoreList);
            map.put("cid",0);
            for (File f : files) {
                index++;
                //前六个为计科
                map.put("major", "会计");
                if (index < 7) {
                    map.put("major", "计算机科学与技术");
                }
                //对6取余即为学期数，6的倍数就是6
                map.put("term", index % 6);
                if (index % 6 == 0) {
                    map.put("term", 6);
                }
                //获取文件路径
                filePath = f.getPath();
                //判断文件名是否为Book开头
                if (f.getName().startsWith("Book")) {
                    readBookXlsx(filePath,map);
                }
            }
        }
        return map;
    }

    /**
     * 从表格获取所有课程和成绩
     *
     * @throws IOException
     */
    @SuppressWarnings("all")
    public static Map<String,Object> getAllCoursesAndScore() throws IOException {
        return findAllXlsx();
    }
}
