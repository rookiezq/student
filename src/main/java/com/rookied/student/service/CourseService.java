package com.rookied.student.service;

import com.rookied.student.bean.Course;

import java.util.List;
import java.util.Map;

/**
 * @desciption: 课程Service
 * @author: Demon
 * @version: 1.0 2019-03-29 20:03
 **/
public interface CourseService extends BaseService<Course> {
    /**
     * 根据学期查找课程
     * @return
     */
    Map<String, List<String>> findCourseByTerm();

    /**
     * 从redis中查找课程
     *
     * @param cmaj  专业
     * @param cterm 学期
     * @return 课程表
     */
    List<String> findCourseByRedis(String cmaj, int cterm);

    /**
     * 查找课程对应成绩
     * @return 课程对应成绩集的map
     */
    Map<String, List<String>> findCourseAndScore();
}
