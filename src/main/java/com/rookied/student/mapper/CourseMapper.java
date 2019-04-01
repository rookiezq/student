package com.rookied.student.mapper;

import com.rookied.student.bean.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @desciption:
 * @author: Demon
 * @version: 1.0 2019-03-28 19:01
 **/
@Mapper
public interface CourseMapper {
    /**
     * 查找所有课程
     *
     * @return 课程列表
     */
    List<Course> findAll();

    /**
     *
     * @return
     */
    List<Course> findCourseAndScore();

    /**
     * 通过学号查找
     *
     * @param id 学号
     * @return 课程列表
     */
    List<Course> findById(int id);

    /**
     * 创建一个课程信息
     *
     * @param student 课程
     */
    void create(Course student);

    /**
     * 删除一个课程信息
     *
     * @param id 学号
     */
    void delete(int id);

    /**
     * 更新一个课程信息
     *
     * @param user 课程类
     */
    void update(Course user);

}
