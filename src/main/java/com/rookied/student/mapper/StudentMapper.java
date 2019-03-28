package com.rookied.student.mapper;

import com.rookied.student.bean.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-28 19:01
 **/
@Mapper
public interface StudentMapper {
    /**
     * 查找所有学生
     *
     * @return 学生列表
     */
    List<Student> findAll();

    /**
     * 通过学号查找
     *
     * @param id 学号
     * @return 学生列表
     */
    List<Student> findById(int id);

    /**
     * 创建一个学生信息
     *
     * @param student 学生
     */
    void create(Student student);

    /**
     * 删除一个学生信息
     *
     * @param id 学号
     */
    void delete(int id);

    /**
     * 更新一个学生信息
     *
     * @param user 学生类
     */
    void update(Student user);

}
