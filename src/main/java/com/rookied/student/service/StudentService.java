package com.rookied.student.service;

import com.rookied.student.bean.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @desciption: 学生Service
 * @author: Demon
 * @version: 1.0 2019-03-29 20:02
 **/
public interface StudentService extends BaseService<Student> {
    /**
     * 通过学号查找所有成绩
     *
     * @param stuId  学号
     * @param cterm  学期
     * @param stuMaj 专业
     * @return 成绩列表
     */
    List<Map<String, String>> findAllScore(String stuId, String cterm, String stuMaj);
}
